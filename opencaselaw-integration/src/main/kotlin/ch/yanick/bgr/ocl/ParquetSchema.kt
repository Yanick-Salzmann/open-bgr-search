package ch.yanick.bgr.ocl

import org.apache.avro.SchemaBuilder
import org.apache.parquet.avro.AvroParquetReader
import org.apache.parquet.io.LocalInputFile
import java.nio.file.Paths

data class CaseRecord(
    val decisionId: String?,
    val court: String?,
    val canton: String?,
    val chamber: String?,
    val branch: String?,
    val proceedingType: String?
)

private val all_fields = listOf(
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

val CASE_SCHEMA = SchemaBuilder.builder()
    .record("Case")
    .fields()
    .also {
        all_fields.forEach { fld ->
            when(fld) {
                "marked_for_publication" -> it.name(fld).type().booleanType().noDefault()
                "has_full_text" -> it.name(fld).type().booleanType().noDefault()
                "text_length" -> it.name(fld).type().intType().noDefault()
                else -> it.name(fld).type().stringType().noDefault()
            }
        }
    }
    .endRecord()

fun main() {
    AvroParquetReader.genericRecordReader(LocalInputFile(Paths.get("./third_party/swiss-caselaw/ag_anwaltskommission.parquet"))).use {
        generateSequence { it.read() }.forEach {
            println(it)
        }
    }
}
