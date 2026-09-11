package ch.yanick.bgr.ocl

interface OpenCaseLawListener {
    fun rootChanged(rootSha: String): Boolean
    fun isNew(filePath: String, sha: String): Boolean
    fun fileCompleted(filePath: String, sha: String)
    fun entryExists(filePath: String, decisionId: String): Boolean
    fun saveEntry(filePath: String, decisionId: String, caseRecord: CaseRecord)
}