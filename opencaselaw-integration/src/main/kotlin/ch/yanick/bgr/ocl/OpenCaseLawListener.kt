package ch.yanick.bgr.ocl

import kotlin.time.Instant

interface OpenCaseLawListener {
    fun rootChanged(rootSha: String): Boolean
    fun isNew(filePath: String, modified: Instant): Boolean
    fun entryExists(filePath: String, decisionId: String): Boolean
    fun saveEntry(filePath: String, decisionId: String, caseRecord: CaseRecord)
}