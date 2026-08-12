package ch.yanick.bgr.config

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    val version: String,
    val entscheidSuche: ES
)