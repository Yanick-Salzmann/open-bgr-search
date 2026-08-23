package ch.yanick.bgr.config

import kotlinx.serialization.Serializable

@Serializable
data class ES(
    val endpoint: String = ""
)

@Serializable
data class DatabaseConfig(
    val jdbcUrl: String = "",
    val username: String = "",
    val password: String = ""
)

@Serializable
data class OpenCaseLaw(
    val repoName: String = ""
)

@Serializable
data class AppConfig(
    val version: String,
    val entscheidSuche: ES = ES(),
    val db: DatabaseConfig = DatabaseConfig(),
    val openCaseLaw: OpenCaseLaw = OpenCaseLaw()
)