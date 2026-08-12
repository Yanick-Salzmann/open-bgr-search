package ch.yanick.bgr.es.mcp.model

typealias ListFacetsInput = Unit

data class ListFacetsOutput(
    val id: String,
    val label: Map<String, String>,
    val children: List<ListFacetsOutput>
)

