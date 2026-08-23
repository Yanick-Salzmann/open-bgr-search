package ch.yanick.bgr.ocl

import kotlinx.serialization.Serializable
import org.apache.avro.generic.GenericRecord
import org.apache.avro.util.Utf8
import org.apache.parquet.avro.AvroParquetReader
import org.apache.parquet.io.LocalInputFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.io.path.extension
import kotlin.io.path.isRegularFile
import kotlin.reflect.full.findParameterByName
import kotlin.time.Instant
import kotlin.time.toKotlinInstant

@Serializable
data class CaseRecord(
    val decisionId: String?,
    val court: String?,
    val canton: String?,
    val chamber: String?,
    val branch: String?,
    val proceedingType: String?,
    val proceduralCode: String?,
    val appealedCourtRaw: String?,
    val appealedData: String?,
    val appealedDocket: String?,
    val docketNumber: String?,
    val docketNumber2: String?,
    val publicationDate: Instant?,
    val markedForPublication: Boolean?,
    val language: String?,
    val title: String?,
    val legalArea: String?,
    val regeste: String?,
    val abstractDe: String?,
    val abstractFr: String?,
    val abstractIt: String?,
    val fullText: String?,
    val outcome: String?,
    val decisionType: String?,
    val judges: String?,
    val clerks: String?,
    val collection: String?,
    val appealInfo: String?,
    val sourceUrl: String?,
    val pdfUrl: String?,
    val bgeReference: String?,
    val citedDecisions: String?,
    val scrapedAt: Instant?,
    val externalId: String?,
    val source: String?,
    val sourceId: String?,
    val sourceSpider: String?,
    val contentHash: String?,
    val hasFullText: Boolean?,
    val textLength: Int?
) {
    companion object {
        private val snakeRegex = Regex("_([a-z0-9])")
        private val ctor = CaseRecord::class.constructors.first { it.parameters.isNotEmpty() }

        private val fieldMap = allFields.associateWith {
            val field = it.replace(snakeRegex) { res ->
                val nonSnake = res.groups[1]?.value ?: ""
                nonSnake.uppercase()
            }

            ctor.findParameterByName(field) ?: throw IllegalStateException("Field not found: $field")
        }


        fun fromRaw(rec: GenericRecord): CaseRecord {
            val resMap = fieldMap.map { (raw, field) ->
                val content = if (rec.hasField(raw)) {
                    when (val data = rec[raw]) {
                        is Utf8 -> data.toString()
                        else -> ""
                    }
                } else ""

                field to when (field.type.classifier) {
                    Int::class -> if (content.isNotEmpty()) content.toInt() else 0
                    Instant::class -> parseDateOrTime(content)
                    Boolean::class -> content.isNotEmpty() && content != "0"
                    else -> content
                }
            }.associate { it }

            return ctor.callBy(resMap)
        }

        private fun parseDateOrTime(content: String): Instant = if (content.isNotEmpty()) {
            if (content.length <= 12) {
                ZonedDateTime.of(LocalDate.parse(content).atStartOfDay(), ZoneOffset.systemDefault()).toInstant()
                    .toKotlinInstant()
            } else {
                if (content.endsWith("Z") || content.contains("+")) {
                    ZonedDateTime.parse(content).toInstant().toKotlinInstant()
                } else {
                    ZonedDateTime.of(LocalDateTime.parse(content), ZoneOffset.systemDefault()).toInstant()
                        .toKotlinInstant()
                }
            }
        } else Instant.fromEpochMilliseconds(0)
    }
}

private val allFields = listOf(
    "decision_id",
    "court",
    "canton",
    "chamber",
    "branch",
    "proceeding_type",
    "procedural_code",
    "appealed_court_raw",
    "appealed_data",
    "appealed_docket",
    "docket_number",
    "docket_number_2",
    "publication_date",
    "marked_for_publication",
    "language",
    "title",
    "legal_area",
    "regeste",
    "abstract_de",
    "abstract_fr",
    "abstract_it",
    "full_text",
    "outcome",
    "decision_type",
    "judges",
    "clerks",
    "collection",
    "appeal_info",
    "source_url",
    "pdf_url",
    "bge_reference",
    "cited_decisions",
    "scraped_at",
    "external_id",
    "source",
    "source_id",
    "source_spider",
    "content_hash",
    "has_full_text",
    "text_length"
)

@Serializable
data class CitationNode(
    val reference: String,
    val citing: MutableList<CitationNode>
)

fun buildCitationGraph(cases: Map<String, CaseRecord>): Map<String, CitationNode> {
    val citationRegex = "\"([^\"]+)\"".toRegex()
    val ret = mutableMapOf<String, CitationNode>()
    cases.forEach { (id, case) ->
        val existingNode = ret.getOrPut(id) { CitationNode(id, mutableListOf()) }
        if (case.citedDecisions?.isBlank() ?: true) {
            return@forEach
        }

        val citations = citationRegex.findAll(case.citedDecisions)
        citations.forEach { cit ->
            val citedRef = cit.groups[1]?.value ?: return@forEach
            if (!cases.containsKey(citedRef)) {
                println("New case: $citedRef")
            }
            val cited = ret.getOrPut(citedRef) { CitationNode(citedRef, mutableListOf()) }
            existingNode.citing.add(cited)
        }
    }

    return ret
}

private val parquetFiles: List<Path> = Files.walk(Paths.get("third_party/swiss-caselaw")).use { strm ->
    strm.filter {
        it.isRegularFile()
    }.filter {
        it.extension == "parquet"
    }.toList()
}

fun readCasesFromData(file: Path): Map<String, CaseRecord> {
    return AvroParquetReader.genericRecordReader(LocalInputFile(file))
        .use { reader ->
            generateSequence { reader.read() }.map {
                CaseRecord.fromRaw(it)
            }.filter {
                it.decisionId != null
            }.associateBy {
                it.decisionId!!
            }
        }

}
