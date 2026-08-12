package ch.yanick.bgr.es.mcp

import ch.yanick.bgr.config.AppConfig
import ch.yanick.bgr.config.ES
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.slf4j.LoggerFactory
import kotlin.test.BeforeTest
import kotlin.test.Test

class ESIntegrationTest {
    private val log = LoggerFactory.getLogger(ESIntegrationTest::class.java)

    private val config = AppConfig(
        version = "1.0.0",
        entscheidSuche = ES(
            endpoint = "https://mcp.entscheidsuche.ch/mcp"
        )
    )

    private val client = EsMcpClient(config)

    @BeforeTest
    fun setup() {
        runBlocking {
            client.connect()
        }
    }

    @Test
    fun `we can parse server info response`() {
        runBlocking {
            val info = client.serverInfo()
            log.info("Server Version: {}", info)
            assertThat(info.name).contains("entscheid")
            assertThat(info.version).isNotBlank()
        }
    }

    @Test
    fun `we can find a decision by id`() {
        runBlocking {
            val info = client.searchByCaseNumber("BGE 142 III 1")
            log.info("Case Info: {}", info)
            assertThat(info.total).isGreaterThan(0)
            assertThat(info.hits.size).isGreaterThan(0)
        }
    }
}