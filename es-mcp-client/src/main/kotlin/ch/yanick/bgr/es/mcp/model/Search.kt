package ch.yanick.bgr.es.mcp.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlin.time.Instant

@Serializable
data class SearchInput(
    val query: String = "*",
    val language: String,
    val sort: String = Sorting.relevance,
    val size: Int = 20,
    val searchAfter: Array<JsonElement>? = null,
    val decisionDateFrom: Instant? = null,
    val decisionDateTo: Instant? = null,
    val scrapeDateFrom: Instant? = null,
    val scrapeDateTo: Instant? = null,
    val hierarchy: Array<String>? = null,
    val languageFilter: String? = null,
    val includeAggregations: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchInput

        if (size != other.size) return false
        if (includeAggregations != other.includeAggregations) return false
        if (query != other.query) return false
        if (language != other.language) return false
        if (sort != other.sort) return false
        if (!searchAfter.contentEquals(other.searchAfter)) return false
        if (decisionDateFrom != other.decisionDateFrom) return false
        if (decisionDateTo != other.decisionDateTo) return false
        if (scrapeDateFrom != other.scrapeDateFrom) return false
        if (scrapeDateTo != other.scrapeDateTo) return false
        if (!hierarchy.contentEquals(other.hierarchy)) return false
        if (languageFilter != other.languageFilter) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size
        result = 31 * result + includeAggregations.hashCode()
        result = 31 * result + query.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + sort.hashCode()
        result = 31 * result + (searchAfter?.contentHashCode() ?: 0)
        result = 31 * result + decisionDateFrom.hashCode()
        result = 31 * result + decisionDateTo.hashCode()
        result = 31 * result + scrapeDateFrom.hashCode()
        result = 31 * result + scrapeDateTo.hashCode()
        result = 31 * result + (hierarchy?.contentHashCode() ?: 0)
        result = 31 * result + languageFilter.hashCode()
        return result
    }
}

@Serializable
data class SearchOutput(
    val total: Int,
    val nextCursor: Array<JsonElement>,
    val hits: Array<Hit>,
    val aggregations: Map<String, Collection<Aggregation>>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchOutput

        if (total != other.total) return false
        if (!nextCursor.contentEquals(other.nextCursor)) return false
        if (!hits.contentEquals(other.hits)) return false
        if (aggregations != other.aggregations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = total
        result = 31 * result + nextCursor.contentHashCode()
        result = 31 * result + hits.contentHashCode()
        result = 31 * result + aggregations.hashCode()
        return result
    }

}