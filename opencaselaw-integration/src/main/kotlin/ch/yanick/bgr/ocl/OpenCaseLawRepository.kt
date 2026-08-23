package ch.yanick.bgr.ocl

import ch.yanick.bgr.config.AppConfig
import ch.yanick.bgr.ocl.model.TreeObject
import ch.yanick.bgr.utils.logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OpenCaseLawRepository(config: AppConfig) {
    private val log by logger()

    private val hfRepo = config.openCaseLaw.repoName
    private val apiBaseUrl = "https://huggingface.co/api/datasets/$hfRepo"
    private val repoBaseUrl = "https://huggingface.co/datasets/$hfRepo"

    private val client = HttpClient()
        .config {
            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                level = LogLevel.HEADERS

            }
        }

    init {
        assert(hfRepo.isNotBlank())
    }

    suspend fun checkForChanges(listener: OpenCaseLawListener) {
        withContext(Dispatchers.IO) {
            val dataSet = fetchDataSet()
            if (!listener.rootChanged("")) {
                return@withContext
            }

            val loader = OpenCaseLawLoader(listener, this@OpenCaseLawRepository)

            log.info("Updating Swiss case law repository from $hfRepo")
            for ((_, type, _, _, _, path, lastCommit) in dataSet) {
                if (type != "file" || !path.endsWith(".parquet", true)) {
                    continue
                }

                if (listener.isNew(path, lastCommit.date)) {
                    loader.processFile(path)
                }
            }
        }
    }

    internal suspend fun fetchFile(fileName: String): ByteReadChannel {
        return client.get("$repoBaseUrl/resolve/main/$fileName").bodyAsChannel()
    }

    private suspend fun fetchDataSet(): List<TreeObject> {
        return downloadPage("$apiBaseUrl/tree/main?expand=true&recursive=true")
    }

    private suspend fun downloadPage(link: String): List<TreeObject> {
        val res = client.get(link)
        val link = res.headers["Link"] ?: return res.body()
        val parts = link.split(";")
        if (parts.size != 2) {
            log.warn(
                "Cannot parse Link header form hugging face, expected two parts in {} got {}. Assuming no pagination.",
                link,
                parts
            )
            return res.body()
        }

        val nextPage = parts[0].trim().removePrefix("<").removeSuffix(">")
        val result = res.body<List<TreeObject>>().toMutableList()
        if (nextPage.isNotBlank()) {
            result.addAll(downloadPage(nextPage))
        }

        return result
    }
}