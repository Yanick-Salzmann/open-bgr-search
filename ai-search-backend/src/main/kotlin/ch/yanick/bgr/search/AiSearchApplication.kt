package ch.yanick.bgr.search

import ch.yanick.bgr.config.AppConfig
import ch.yanick.bgr.config.ConfigLoader
import ch.yanick.bgr.ocl.CaseRecord
import ch.yanick.bgr.ocl.OpenCaseLawListener
import ch.yanick.bgr.ocl.OpenCaseLawRepository
import ch.yanick.bgr.search.db.CaseRecordRepository
import ch.yanick.bgr.search.db.configureDatabaseModule
import io.ktor.server.application.log
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.di.dependencies
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Instant

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

                override fun isNew(filePath: String, modified: Instant): Boolean {
                    return true
                }

                override fun entryExists(filePath: String, decisionId: String): Boolean {
                    return false
                }

                override fun saveEntry(filePath: String, decisionId: String, caseRecord: CaseRecord) {
                    caseRecord.decisionId ?: return
                    runBlocking {
                        caseRepo.saveCase(caseRecord)
                    }
                }
            }))
        }
    }

    server.start(true)
}