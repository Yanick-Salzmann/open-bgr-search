package ch.yanick.bgr.search.db

import ch.yanick.bgr.ocl.CaseRecord
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass
import org.jetbrains.exposed.v1.datetime.datetime
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import kotlin.time.Instant

object CaseRecordTable : IdTable<String>("case_record") {
    override val id = varchar("id", 500).entityId()
    val court = text("court").nullable()
    val canton = text("canton").nullable()
    val chamber = text("chamber").nullable()
    val branch = text("branch").nullable()
    val proceedingType = text("proceedingtype").nullable()
    val proceduralCode = text("proceduralcode").nullable()
    val appealedCourt = text("appealedcourt").nullable()
    val appealedDocket = text("appealeddocket").nullable()
    val docketNumber = text("docketnumber").nullable()
    val publicationDate = datetime("publicationdate")
    val language = varchar("language", 10).nullable()
    val title = text("title").nullable()
    val legalArea = text("legalarea").nullable()
    val regeste = text("regeste").nullable()
    val abstractDe = text("abstractde").nullable()
    val abstractFr = text("abstractfr").nullable()
    val abstractIt = text("abstractit").nullable()
    val fullText = text("fulltext").nullable()
    val outcome = text("outcome").nullable()
    val sourceUrl = text("sourceurl").nullable()
    val pdfUrl = text("pdfurl").nullable()
    val externalId = text("externalid").nullable()
}

class CaseRecordEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, CaseRecordEntity>(CaseRecordTable)

    var court by CaseRecordTable.court
    var canton by CaseRecordTable.canton
    var chamber by CaseRecordTable.chamber
    var branch by CaseRecordTable.branch
    var proceedingType by CaseRecordTable.proceedingType
    var proceduralCode by CaseRecordTable.proceduralCode
    var appealedCourt by CaseRecordTable.appealedCourt
    var appealedDocket by CaseRecordTable.appealedDocket
    var docketNumber by CaseRecordTable.docketNumber
    var publicationDate by CaseRecordTable.publicationDate
    var language by CaseRecordTable.language
    var title by CaseRecordTable.title
    var legalArea by CaseRecordTable.legalArea
    var regeste by CaseRecordTable.regeste
    var abstractDe by CaseRecordTable.abstractDe
    var abstractFr by CaseRecordTable.abstractFr
    var abstractIt by CaseRecordTable.abstractIt
    var fullText by CaseRecordTable.fullText
    var outcome by CaseRecordTable.outcome
    var sourceUrl by CaseRecordTable.sourceUrl
    var pdfUrl by CaseRecordTable.pdfUrl
    var externalId by CaseRecordTable.externalId
}

class CaseRecordRepository {
    suspend fun findCaseById(decisionId: String) = suspendTransaction {
        CaseRecordEntity.findById(decisionId)
    }

    suspend fun caseExists(decisionId: String) = findCaseById(decisionId) != null

    suspend fun upsertCase(record: CaseRecord) = suspendTransaction {
        val id = record.decisionId ?: error("decisionId is null")
        if (caseExists(id)) {
            updateCase(id, record)
        } else {
            saveCase(record)
        }
    }

    suspend fun updateCase(id: String, record: CaseRecord) = suspendTransaction {
        if (id != record.decisionId) {
            error("decisionId mismatch")
        }

        CaseRecordEntity.findByIdAndUpdate(id) {
            copyRecordFieldsFrom(it, record)
        }
    }

    suspend fun saveCase(record: CaseRecord) = suspendTransaction {
        CaseRecordEntity.new(record.decisionId) {
            copyRecordFieldsFrom(this, record)
        }
    }

    private fun copyRecordFieldsFrom(target: CaseRecordEntity, source: CaseRecord) {
        target.apply {
            court = source.court
            canton = source.canton
            chamber = source.chamber
            branch = source.branch
            proceedingType = source.proceedingType
            proceduralCode = source.proceduralCode
            appealedCourt = source.appealedCourtRaw
            appealedDocket = source.appealedDocket
            docketNumber = source.docketNumber
            publicationDate = (source.publicationDate
                ?: Instant.fromEpochMilliseconds(0)).toLocalDateTime(TimeZone.currentSystemDefault())
            language = source.language
            title = source.title
            legalArea = source.legalArea
            regeste = source.regeste
            abstractDe = source.abstractDe
            abstractFr = source.abstractFr
            abstractIt = source.abstractIt
            fullText = source.fullText
            outcome = source.outcome
            sourceUrl = source.sourceUrl
            pdfUrl = source.pdfUrl
            externalId = source.externalId
        }
    }
}