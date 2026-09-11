package ch.yanick.bgr.search.db

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

object CaseRepoFileTable : IntIdTable("case_repo_file") {
    val filePath = varchar("file_path", 1024)
    val sha = varchar("sha", 40)
}

class CaseRepoFileEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CaseRepoFileEntity>(CaseRepoFileTable)

    var filePath by CaseRepoFileTable.filePath
    var sha by CaseRepoFileTable.sha
}

object CaseRepoFileRepository {
    suspend fun shaForFile(file: String) = suspendTransaction {
        CaseRepoFileEntity.find {
            CaseRepoFileTable.filePath eq file
        }.map {
            it.sha
        }.firstOrNull()
    }

    suspend fun upsert(filePath: String, sha: String) = suspendTransaction {
        CaseRepoFileEntity.findSingleByAndUpdate(CaseRepoFileTable.filePath eq filePath) {
            it.sha = sha
        } ?: CaseRepoFileEntity.new {
            this.filePath = filePath
            this.sha = sha
        }
    }
}