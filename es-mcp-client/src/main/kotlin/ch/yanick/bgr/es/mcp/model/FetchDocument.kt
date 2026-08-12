package ch.yanick.bgr.es.mcp.model

data class FetchDocumentInput(
    val id: String,
    val language: String? = null
)

data class FetchDocumentOutput(
    val result: Hit? = null
)