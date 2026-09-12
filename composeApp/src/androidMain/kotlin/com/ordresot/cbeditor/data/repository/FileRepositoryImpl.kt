package com.ordresot.cbeditor.data.repository

import com.ordresot.cbeditor.domain.repository.ArchiveRepository
import com.ordresot.cbeditor.domain.repository.FileRepository
import com.ordresot.cbeditor.domain.repository.PreferencesRepository
import com.ordresot.cbeditor.models.ArchiveEntry
import com.ordresot.cbeditor.utils.MergeProgressTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

actual class FileRepositoryImpl actual constructor() : FileRepository, KoinComponent {
    private val archiveService: ArchiveRepository by inject()
    private val preferencesManager: PreferencesRepository by inject()

    actual override suspend fun mergeFiles(
        sourcePaths: List<String>,
        targetPath: String,
        progressTracker: MergeProgressTracker?
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            if (sourcePaths.size < 2) {
                return@withContext false
            }

            progressTracker?.updateProgress(0f, "Проверка файлов...")

            val validFiles = sourcePaths.filter { path ->
                val file = File(path)
                file.exists() && (file.extension.lowercase() in listOf("cbz", "cbr"))
            }

            if (validFiles.size < 2) {
                return@withContext false
            }

            progressTracker?.updateProgress(0.1f, "Подготовка к объединению...")

            val allEntries = mutableListOf<ArchiveEntry>()

            validFiles.forEachIndexed { archiveIndex, sourcePath ->
                val progressForThisArchive = 0.1f + (archiveIndex.toFloat() / validFiles.size.toFloat() * 0.7f)
                val file = File(sourcePath)
                progressTracker?.updateProgress(
                    progress = progressForThisArchive,
                    operation = "Обработка: ${file.name} (${archiveIndex + 1}/${validFiles.size})"
                )

                val entries = archiveService.extractFiles(sourcePath, progressTracker)
                if (entries.isNotEmpty()) {
                    allEntries.addAll(entries)
                }
            }

            if (allEntries.isEmpty()) {
                return@withContext false
            }

            progressTracker?.updateProgress(0.8f, "Создание объединенного архива...")

            val success = archiveService.createArchive(allEntries, targetPath, progressTracker)

            if (success) {
                File(targetPath).parentFile?.let { preferencesManager.saveLastDirectory(it) }
                progressTracker?.updateProgress(1f, "Объединение завершено!")
            }

            success
        } catch (e: Exception) {
            false
        }
    }

    actual override suspend fun readFile(path: String): ByteArray? = withContext(Dispatchers.IO) {
        try {
            File(path).readBytes()
        } catch (e: Exception) {
            null
        }
    }

    actual override suspend fun fileExists(path: String): Boolean = withContext(Dispatchers.IO) {
        File(path).exists()
    }

    actual override suspend fun saveFile(data: ByteArray, targetPath: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                File(targetPath).writeBytes(data)
                true
            } catch (e: Exception) {
                false
            }
        }
}
