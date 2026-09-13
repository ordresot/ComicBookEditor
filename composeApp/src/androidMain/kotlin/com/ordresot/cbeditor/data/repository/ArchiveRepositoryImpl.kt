package com.ordresot.cbeditor.data.repository

import com.ordresot.cbeditor.domain.repository.ArchiveRepository
import com.ordresot.cbeditor.models.ArchiveEntry
import com.ordresot.cbeditor.presentation.utils.MergeProgressTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.zip.ZipFile

actual class ArchiveRepositoryImpl actual constructor() : ArchiveRepository {
    actual override suspend fun extractFiles(
        archivePath: String,
        progressTracker: MergeProgressTracker?
    ): List<ArchiveEntry> = withContext(Dispatchers.IO) {
        try {
            val file = File(archivePath)
            if (!file.exists()) return@withContext emptyList()

            when {
                file.extension.lowercase() in listOf("cbz", "zip") ->
                    extractZipFiles(archivePath, progressTracker)
                file.extension.lowercase() in listOf("cbr", "rar") ->
                    extractZipFiles(archivePath, progressTracker) // Android: пробуем как ZIP
                else -> emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    actual override suspend fun createArchive(
        entries: List<ArchiveEntry>,
        outputPath: String,
        progressTracker: MergeProgressTracker?
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            java.util.zip.ZipOutputStream(java.io.BufferedOutputStream(java.io.FileOutputStream(outputPath))).use { zipOutput ->
                entries.forEachIndexed { index, entry ->
                    progressTracker?.updateProgress(
                        progress = index.toFloat() / entries.size.toFloat(),
                        operation = "Создание архива (${index + 1}/${entries.size})"
                    )
                    val safeName = normalizeFileName(entry.name, index + 1, entries.size)
                    zipOutput.putNextEntry(java.util.zip.ZipEntry(safeName))
                    zipOutput.write(entry.data)
                    zipOutput.closeEntry()
                }
            }
            progressTracker?.updateProgress(1f, "Архив создан!")
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun extractZipFiles(zipPath: String, progressTracker: MergeProgressTracker?): List<ArchiveEntry> {
        val entries = mutableListOf<ArchiveEntry>()
        try {
            ZipFile(File(zipPath)).use { zip ->
                val allEntries = zip.entries().toList()
                    .filter { !it.isDirectory && isImageFile(it.name) }
                    .sortedBy { it.name }

                allEntries.forEachIndexed { index, entry ->
                    progressTracker?.updateProgress(
                        progress = index.toFloat() / allEntries.size.toFloat(),
                        operation = "Извлечение: ${File(zipPath).name} (${index + 1}/${allEntries.size})"
                    )
                    zip.getInputStream(entry).use { input ->
                        val data = input.readAllBytes()
                        entries.add(ArchiveEntry(entry.name, data, zipPath))
                    }
                }
            }
        } catch (e: Exception) {
            // Return empty list on error
        }
        return entries
    }

    private fun normalizeFileName(originalName: String, index: Int, totalFiles: Int): String {
        val cleanName = originalName.substringAfterLast('/').substringAfterLast('\\')
        val extension = cleanName.substringAfterLast('.', "").lowercase()
        val padding = when {
            totalFiles >= 100000 -> 6
            totalFiles >= 10000 -> 5
            totalFiles >= 1000 -> 4
            totalFiles >= 100 -> 3
            totalFiles >= 10 -> 2
            else -> 1
        }
        return "%0${padding}d.%s".format(index, extension.lowercase())
    }

    private fun isImageFile(filename: String): Boolean {
        val imageExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
        val extension = filename.substringAfterLast('.', "").lowercase()
        return imageExtensions.contains(extension)
    }
}
