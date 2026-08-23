package ch.yanick.bgr.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig

object ConfigLoader {
    @OptIn(ExperimentalSerializationApi::class)
    fun loadConfig(): AppConfig {
        val rootCfg = ConfigFactory.load()
        val cfg = (loadEnvConfig()?.withFallback(rootCfg) ?: rootCfg).withFallback(ConfigFactory.systemEnvironment())
        return Hocon.decodeFromConfig(cfg.getConfig("application"))
    }

    private fun loadEnvConfig(): Config? {
        val env = System.getenv("application.environment")
        if (env.isNullOrBlank()) {
            return null
        }

        return ConfigFactory.load("application-${env}.conf")
    }
}