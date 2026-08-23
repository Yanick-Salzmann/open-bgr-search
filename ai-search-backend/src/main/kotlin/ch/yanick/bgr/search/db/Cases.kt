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
    val court = varchar("court", 250).nullable()
    val canton = varchar("canton", 50).nullable()
    val chamber = varchar("chamber", 100).nullable()
    val branch = varchar("branch", 100).nullable()
    val proceedingType = varchar("proceedingtype", 50).nullable()
    val proceduralCode = varchar("proceduralcode", 50).nullable()
    val appealedCourt = varchar("appealedcourt", 250).nullable()
    val appealedDocket = varchar("appealeddocket", 50).nullable()
    val docketNumber = varchar("docketnumber", 500).nullable()
    val publicationDate = datetime("publicationdate")
    val language = varchar("language", 10).nullable()
    val title = text("title").nullable()
    val legalArea = varchar("legalarea", 50).nullable()
    val regeste = text("regeste").nullable()
    val abstractDe = text("abstractde").nullable()
    val abstractFr = text("abstractfr").nullable()
    val abstractIt = text("abstractit").nullable()
    val fullText = text("fulltext").nullable()
    val outcome = text("outcome").nullable()
    val sourceUrl = varchar("sourceurl", 1024).nullable()
    val pdfUrl = varchar("pdfurl", 1024).nullable()
    val externalId = varchar("externalid", 100).nullable()
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
    suspend fun saveCase(record: CaseRecord) = suspendTransaction {
        CaseRecordEntity.new(record.decisionId) {
            court = record.court
            canton = record.canton
            chamber = record.chamber
            branch = record.branch
            proceedingType = record.proceedingType
            proceduralCode = record.proceduralCode
            appealedCourt = record.appealedCourtRaw
            appealedDocket = record.appealedDocket
            docketNumber = record.docketNumber
            publicationDate = (record.publicationDate ?: Instant.fromEpochMilliseconds(0)).toLocalDateTime(TimeZone.currentSystemDefault())
            language = record.language
            title = record.title
            legalArea = record.legalArea
            regeste = record.regeste
            abstractDe = record.abstractDe
            abstractFr = record.abstractFr
            abstractIt = record.abstractIt
            fullText = record.fullText
            outcome = record.outcome
            sourceUrl = record.sourceUrl
            pdfUrl = record.pdfUrl
            externalId = record.externalId
        }
    }
}