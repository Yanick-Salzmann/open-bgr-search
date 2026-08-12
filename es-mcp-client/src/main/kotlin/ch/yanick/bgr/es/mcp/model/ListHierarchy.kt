package ch.yanick.bgr.es.mcp.model

data class ListHierarchyInput(
    val query: String = "*",
    val size: Int = 1000
)

data class ListHierarchyOutput(
    val entries: Array<Aggregation> = emptyArray(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListHierarchyOutput

        return entries.contentEquals(other.entries)
    }

    override fun hashCode(): Int {
        return entries.contentHashCode()
    }
}