package ch.yanick.bgr.es.mcp

import ch.yanick.bgr.config.AppConfig
import ch.yanick.bgr.es.mcp.model.SearchByCaseNumberInput
import ch.yanick.bgr.es.mcp.model.SearchByCaseNumberOutput
import ch.yanick.bgr.es.mcp.model.ServerInfoOutput
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.ReconnectionOptions
import io.modelcontextprotocol.kotlin.sdk.client.StreamableHttpClientTransport
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.json.decodeFromJsonElement
import org.slf4j.LoggerFactory

class EsMcpClient(private val config: AppConfig) {
    private val log = LoggerFactory.getLogger(EsMcpClient::class.java)

    private val httpClient = HttpClient(CIO)

    private val mcpClient = Client(
        clientInfo = Implementation(
            name = "OpenBGRSearch-client",
            version = config.version
        )
    )

    suspend fun connect() {
        mcpClient.connect(StreamableHttpClientTransport(httpClient, config.entscheidSuche.endpoint, ReconnectionOptions()))
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        isLenient = true
        namingStrategy = JsonNamingStrategy.SnakeCase
    }

    suspend fun searchByCaseNumber(caseNumber: String): SearchByCaseNumberOutput {
        val result = mcpClient.callTool("search_by_case_number", SearchByCaseNumberInput(caseNumber).toMap())
        log.info("Call: {}", result)
        val resultObj = result.structuredContent ?: throw IllegalStateException("entscheidsuche: searchByCasseNumber returned no object")
        return json.decodeFromJsonElement(resultObj)
    }

    suspend fun serverInfo(): ServerInfoOutput {
        val result = mcpClient.callTool("server_info", emptyMap())
        val resultObj = result.structuredContent ?: throw IllegalStateException("entscheidsuche: ServerInfo returned no object")
        return json.decodeFromJsonElement(resultObj)
    }
}