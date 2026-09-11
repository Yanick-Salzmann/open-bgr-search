package ch.yanick.bgr.search

import ch.yanick.bgr.config.AppConfig
import ch.yanick.bgr.config.ConfigLoader
import ch.yanick.bgr.ocl.CaseRecord
import ch.yanick.bgr.ocl.OpenCaseLawListener
import ch.yanick.bgr.ocl.OpenCaseLawRepository
import ch.yanick.bgr.search.db.CaseRecordRepository
import ch.yanick.bgr.search.db.CaseRepoFileRepository
import ch.yanick.bgr.search.db.configureDatabaseModule
import io.ktor.server.application.log
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.di.dependencies
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(vararg args: String) {
    val server = embeddedServer(Netty, port = 8800) {
        dependencies {
            provide {
                ConfigLoader.loadConfig()
            }
        }
        configureDatabaseModule()

        val appCfg: AppConfig by dependencies
        val ocsRepo = OpenCaseLawRepository(appCfg)
        val caseRepo: CaseRecordRepository by dependencies

        launch {
            log.info("Data: {}", ocsRepo.checkForChanges(object : OpenCaseLawListener {
                override fun rootChanged(rootSha: String): Boolean {
                    return true
                }

                override fun isNew(filePath: String, sha: String): Boolean {
                    return runBlocking {
                        CaseRepoFileRepository.shaForFile(filePath) != sha
                    }
                }

                override fun fileCompleted(filePath: String, sha: String) {
                    runBlocking {
                        CaseRepoFileRepository.upsert(filePath, sha)
                    }
                }

                override fun entryExists(filePath: String, decisionId: String): Boolean = runBlocking {
                    if(decisionId.isBlank()) {
                        return@runBlocking false
                    }
                    caseRepo.caseExists("$filePath.$decisionId")
                }

                override fun saveEntry(filePath: String, decisionId: String, caseRecord: CaseRecord) {
                    val decision = caseRecord.decisionId ?: return
                    if (decision.isBlank()) {
                        return
                    }

                    if(decision.length > 500) {
                        log.warn("Ignoring decision $decision, ID too long")
                        return
                    }

                    val caseId = "$filePath.$decisionId"
                    runBlocking {
                        caseRepo.upsertCase(caseRecord.copy(decisionId = caseId))
                    }
                }
            }))
        }
    }

    server.start(true)
}