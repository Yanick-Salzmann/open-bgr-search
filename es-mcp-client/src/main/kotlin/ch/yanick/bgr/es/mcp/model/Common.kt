package ch.yanick.bgr.es.mcp.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


object Sorting {
    const val relevance = "relevance"
    const val date = "date"
    const val scrapedate = "scrapedate"
}

object Language {
    const val DE = "de"
    const val FR = "fr"
    const val IT = "it"
}

@Serializable
data class Hit(
    val id: String,
    val title: String,
    val abstract: String,
    val text: String,
    val textTruncated: String?,
    val meta: String,
    val canton: String,
    val court: String,
    val decisionDate: LocalDate,
    val scrapeDate: LocalDate,
    val isPdf: Boolean,
    val documentUrl: String,
    val originalUrl: String?,
    val sort: Array<JsonElement>? = null,
    val contentLength: Int?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hit

        if (isPdf != other.isPdf) return false
        if (id != other.id) return false
        if (title != other.title) return false
        if (abstract != other.abstract) return false
        if (text != other.text) return false
        if (meta != other.meta) return false
        if (canton != other.canton) return false
        if (court != other.court) return false
        if (decisionDate != other.decisionDate) return false
        if (scrapeDate != other.scrapeDate) return false
        if (documentUrl != other.documentUrl) return false
        if (originalUrl != other.originalUrl) return false
        if (!sort.contentEquals(other.sort)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isPdf.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + abstract.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + meta.hashCode()
        result = 31 * result + canton.hashCode()
        result = 31 * result + court.hashCode()
        result = 31 * result + decisionDate.hashCode()
        result = 31 * result + scrapeDate.hashCode()
        result = 31 * result + documentUrl.hashCode()
        result = 31 * result + originalUrl.hashCode()
        result = 31 * result + (sort?.contentHashCode() ?: 0)
        return result
    }
}

@Serializable
data class Aggregation(
    val key: String,
    val count: Int
)