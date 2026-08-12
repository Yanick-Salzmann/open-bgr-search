package ch.yanick.bgr.es.mcp.model

import kotlinx.serialization.Serializable

typealias ServerInfoInput = Unit

@Serializable
data class ServerInfoOutput(
    val name: String,
    val version: String,
    val elasticsearchUrl: String,
    val facetsUrl: String,
    val languages: List<String>,
    val sortOrders: List<String>
)