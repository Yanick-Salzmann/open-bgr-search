package ch.yanick.bgr.ocl

import io.ktor.utils.io.jvm.javaio.toInputStream
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.deleteIfExists

class OpenCaseLawLoader(private val listener: OpenCaseLawListener, private val repo: OpenCaseLawRepository) {
    suspend fun processFile(fileName: String) {
        repo.fetchFile(fileName).toInputStream().use { stream ->
            val tmpFile = Files.createTempFile("ocl-parquet", Paths.get(fileName).fileName.toString())
            tmpFile.toFile().deleteOnExit()
            try {
                Files.newOutputStream(tmpFile).use { fos -> stream.copyTo(fos) }
                readCasesFromData(tmpFile)
            } finally {
                tmpFile.deleteIfExists()
            }
        }.forEach { (decision, record) ->
            listener.saveEntry(fileName, decision, record)
        }
    }
}