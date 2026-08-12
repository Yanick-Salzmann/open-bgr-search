package ch.yanick.bgr.config

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig

object ConfigLoader {
    @OptIn(ExperimentalSerializationApi::class)
    fun loadConfig(): AppConfig {
        val cfg = ConfigFactory.load()
        return Hocon.decodeFromConfig(cfg)
    }
}