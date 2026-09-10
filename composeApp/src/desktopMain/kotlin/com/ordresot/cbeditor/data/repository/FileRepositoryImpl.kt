package com.ordresot.cbeditor.data.repository

import com.ordresot.cbeditor.domain.repository.FileRepository
import com.ordresot.cbeditor.models.ArchiveEntry
import com.ordresot.cbeditor.data.repository.PreferencesRepositoryImpl
import com.ordresot.cbeditor.utils.MergeProgressTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class FileRepositoryImpl actual constructor() : FileRepository {
    private val archiveService = ArchiveRepositoryImpl()
    private val preferencesManager = PreferencesRepositoryImpl()

    actual override suspend fun mergeFiles(
        sourcePaths: List<String>,
        targetPath: String,
        progressTracker: MergeProgressTracker?
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            if (sourcePaths.size < 2) {
                println("Для объединения нужно как минимум 2 файла")
                return@withContext false
            }

            progressTracker?.updateProgress(0f, "Проверка файлов...")

            // Проверяем что все файлы существуют и имеют правильный формат
            val validFiles = sourcePaths.filter { path ->
                val file = File(path)
                val isValid = file.exists() && (file.extension.lowercase() in listOf("cbz", "cbr"))
                if (!isValid) {
                    println("Пропущен невалидный файл: $path")
                }
                isValid
            }

            if (validFiles.size < 2) {
                println("Недостаточно валидных файлов для объединения")
                return@withContext false
            }

            progressTracker?.updateProgress(0.1f, "Подготовка к объединению...")

            // Извлекаем все файлы из исходных архивов
            val allEntries = mutableListOf<ArchiveEntry>()

            validFiles.forEachIndexed { archiveIndex, sourcePath ->
                val progressForThisArchive =
                    0.1f + (archiveIndex.toFloat() / validFiles.size.toFloat() * 0.7f)
                val file = File(sourcePath)
                progressTracker?.updateProgress(
                    progress = progressForThisArchive,
                    operation = "Обработка: ${file.name} (${archiveIndex + 1}/${validFiles.size})"
                )

                println("Обработка файла: ${file.name} (${file.extension})")
                val entries = archiveService.extractFiles(sourcePath, progressTracker)

                if (entries.isEmpty()) {
                    println("Не удалось извлечь файлы из: ${file.name}")
                    // Продолжаем с другими файлами вместо полной остановки
                } else {
                    println("Извлечено ${entries.size} файлов из ${file.name}")
                    allEntries.addAll(entries)
                }
            }

            if (allEntries.isEmpty()) {
                println("Не удалось извлечь файлы ни из одного архива")
                return@withContext false
            }

            println("Найдено всего файлов: ${allEntries.size}")

            progressTracker?.updateProgress(0.8f, "Создание объединенного архива...")

            // Создаем новый архив (всегда CBZ для совместимости)
            val success = archiveService.createArchive(allEntries, targetPath, progressTracker)

            if (success) {
                println("Файлы успешно объединены в: $targetPath")
                // Сохраняем директорию результата
                File(targetPath).parentFile?.let { preferencesManager.saveLastDirectory(it) }
                progressTracker?.updateProgress(1f, "Объединение завершено!")
            } else {
                println("Ошибка при создании объединенного архива")
                progressTracker?.updateProgress(0f, "Ошибка при объединении")
            }

            success
        } catch (e: Exception) {
            println("Ошибка при объединении файлов: ${e.message}")
            e.printStackTrace()
            progressTracker?.updateProgress(0f, "Ошибка: ${e.message}")
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