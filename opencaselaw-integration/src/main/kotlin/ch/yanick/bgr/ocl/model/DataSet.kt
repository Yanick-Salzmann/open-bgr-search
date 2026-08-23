package ch.yanick.bgr.ocl.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlin.time.Instant

data class Sibling(
    val rfilename: String
)

data class CardData(
    val license: String,
    val language: Collection<String>,
    val tags: Collection<String>,
    val prettyName: String,
    val authors: Collection<String>,
    val sizeCategories: List<String>,
    val taskCategories: List<String>,
    val configs: List<Map<String, Any>>
)

data class DataSet(
    val _id: String,
    val id: String,
    val author: String,
    val sha: String,
    val lastModified: Instant,
    val private: Boolean,
    val gated: Boolean,
    val disabled: Boolean,
    val tags: Collection<String>,
    val description: String,
    val downloads: Int,
    val likes: Int,
    val siblings: Collection<Sibling>,
    val createdAt: Instant,
    val usedStorage: Long
)

@Serializable
data class TreeLfs(
    val oid: String,
    val size: Long,
    val pointerSize: Long
)

@Serializable
data class TreeCommit(
    val id: String,
    val title: String,
    val date: Instant
)

@Serializable
data class TreeObject(
    val oid: String,
    val type: String,
    val size: Long,
    val lfs: TreeLfs? = null,
    val xetHash: String? = null,
    val path: String,
    val lastCommit: TreeCommit,
    val securityFileStatus: JsonObject? = null,
    val uploadedAt: Instant? = null
)