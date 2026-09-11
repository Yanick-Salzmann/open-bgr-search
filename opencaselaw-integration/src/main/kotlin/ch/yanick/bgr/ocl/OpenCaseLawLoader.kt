package ch.yanick.bgr.ocl

import io.ktor.utils.io.jvm.javaio.toInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.deleteIfExists

class OpenCaseLawLoader(private val listener: OpenCaseLawListener, private val repo: OpenCaseLawRepository) {
    suspend fun processFile(fileName: String) {
        var tmpFile: Path? = null
        try {
            repo.fetchFile(fileName).toInputStream().use { stream ->
                tmpFile = Files.createTempFile("ocl-parquet", Paths.get(fileName).fileName.toString())
                tmpFile.toFile().deleteOnExit()
                Files.newOutputStream(tmpFile).use { fos -> stream.copyTo(fos) }
                readCasesFromData(tmpFile)
            }.forEach { record ->
                val decisionId = record.decisionId ?: return@forEach
                listener.saveEntry(fileName, decisionId, record)
            }
        } finally {
            tmpFile?.deleteIfExists()
        }
    }
}