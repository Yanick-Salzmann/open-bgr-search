package ch.yanick.bgr.es.mcp.model

import kotlin.time.Instant


data class SearchByCaseNumberInput(
    val caseNumber: String,
    val language: String? = null,
    val sort: String = Sorting.relevance,
    val size: Int = 20,
    val searchAfter: Instant? = null,
    val decisionDateFrom: Instant? = null,
    val decisionDateTo: Instant? = null,
    val scrapeDateFrom: Instant? = null,
    val scrapeDateTo: Instant? = null,
    val hierarchy: Array<String>? = null,
    val languageFilter: String? = null,
    val includeAggregation: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchByCaseNumberInput

        if (size != other.size) return false
        if (includeAggregation != other.includeAggregation) return false
        if (caseNumber != other.caseNumber) return false
        if (language != other.language) return false
        if (sort != other.sort) return false
        if (searchAfter != other.searchAfter) return false
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
        result = 31 * result + includeAggregation.hashCode()
        result = 31 * result + caseNumber.hashCode()
        result = 31 * result + language.hashCode()
        result = 31 * result + sort.hashCode()
        result = 31 * result + searchAfter.hashCode()
        result = 31 * result + decisionDateFrom.hashCode()
        result = 31 * result + decisionDateTo.hashCode()
        result = 31 * result + scrapeDateFrom.hashCode()
        result = 31 * result + scrapeDateTo.hashCode()
        result = 31 * result + (hierarchy?.contentHashCode() ?: 0)
        result = 31 * result + languageFilter.hashCode()
        return result
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "case_number" to caseNumber,
            "language" to language,
            "sort" to sort,
            "size" to size,
            "search_after" to searchAfter,
            "decision_date_from" to decisionDateFrom,
            "decision_date_to" to decisionDateTo,
            "scrape_date_from" to scrapeDateFrom,
            "scrape_date_to" to scrapeDateTo,
            "hierarchy" to hierarchy,
            "language_filter" to languageFilter,
            "include_aggregation" to includeAggregation
        )
    }
}

typealias SearchByCaseNumberOutput = SearchOutput