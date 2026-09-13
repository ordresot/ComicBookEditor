package com.ordresot.cbeditor.data.repository

import com.ordresot.cbeditor.domain.repository.ArchiveRepository
import com.ordresot.cbeditor.models.ArchiveEntry
import com.ordresot.cbeditor.presentation.utils.MergeProgressTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

actual class ArchiveRepositoryImpl actual constructor() : ArchiveRepository {
    actual override suspend fun extractFiles(archivePath: String, progressTracker: MergeProgressTracker?): List<ArchiveEntry> =
        withContext(Dispatchers.IO) {
            try {
                when {
                    archivePath.endsWith(".cbz", ignoreCase = true) ->
                        extractZipFiles(archivePath, progressTracker)

                    archivePath.endsWith(".cbr", ignoreCase = true) -> {
                        // Сначала пробуем как ZIP, потом как RAR
                        tryExtractCbrAsZip(archivePath, progressTracker)
                            ?: extractRarWithExternalTools(archivePath, progressTracker)
                    }

                    else -> emptyList()
                }
            } catch (e: Exception) {
                println("Ошибка при извлечении файлов из $archivePath: ${e.message}")
                emptyList()
            }
        }

    /**
     * Пытаемся открыть CBR файл как ZIP архив (простой способ)
     */
    private suspend fun tryExtractCbrAsZip(cbrPath: String, progressTracker: MergeProgressTracker?): List<ArchiveEntry>? {
        return try {
            println("🔧 Пробуем открыть CBR как ZIP: $cbrPath")
            extractZipFiles(cbrPath, progressTracker).also {
                if (it.isNotEmpty()) {
                    println("CBR файл успешно открыт как ZIP архив")
                }
            }
        } catch (e: Exception) {
            println("CBR файл не является ZIP архивом: ${e.message}")
            null
        }
    }

    private suspend fun extractRarWithExternalTools(rarPath: String, progressTracker: MergeProgressTracker?): List<ArchiveEntry> {
        val entries = mutableListOf<ArchiveEntry>()

        println("CBR не ZIP, пробуем внешние утилиты: $rarPath")

        try {
            val rarFile = File(rarPath)
            if (!rarFile.exists()) {
                println("Файл не существует: $rarPath")
                return emptyList()
            }

            val tempDir = createTempDir("cbr_extract_${System.currentTimeMillis()}")
            println("Временная директория: ${tempDir.absolutePath}")

            val extractionTools = mutableListOf<List<String>>()

            val possible7zPaths = listOf(
                "C:\\Program Files\\7-Zip\\7z.exe",
                "C:\\Program Files (x86)\\7-Zip\\7z.exe",
                "C:\\Program Files\\7-Zip\\7zG.exe",
                "7z", "7z.exe"
            )

            val possibleWinRarPaths = listOf(
                "C:\\Program Files\\WinRAR\\WinRAR.exe",
                "C:\\Program Files (x86)\\WinRAR\\WinRAR.exe",
                "winrar", "WinRAR.exe"
            )

            val possibleUnrarPaths = listOf(
                "C:\\Program Files\\WinRAR\\UnRAR.exe",
                "C:\\Program Files (x86)\\WinRAR\\UnRAR.exe",
                "unrar", "UnRAR.exe"
            )

            // Добавляем только существующие пути
            possible7zPaths.forEach { path ->
                if (File(path).exists() || path.contains(File.separator).not()) {
                    extractionTools.add(listOf(path, "x", "-y", rarFile.absolutePath, "-o${tempDir.absolutePath}"))
                }
            }

            possibleWinRarPaths.forEach { path ->
                if (File(path).exists() || path.contains(File.separator).not()) {
                    extractionTools.add(listOf(path, "x", "-y", rarFile.absolutePath, tempDir.absolutePath + File.separator))
                }
            }

            possibleUnrarPaths.forEach { path ->
                if (File(path).exists() || path.contains(File.separator).not()) {
                    extractionTools.add(listOf(path, "x", "-y", rarFile.absolutePath, tempDir.absolutePath))
                }
            }

            extractionTools.addAll(listOf(
                listOf("rar", "x", "-y", rarFile.absolutePath, tempDir.absolutePath),
                listOf("bsdtar", "-xf", rarFile.absolutePath, "-C", tempDir.absolutePath)
            ))

            println("Доступные утилиты для проверки: ${extractionTools.size}")

            var extractionSuccess = false
            var usedTool = ""

            for (tool in extractionTools) {
                val toolName = tool[0]
                val toolFile = File(toolName)

                if (toolName.contains(File.separator) && !toolFile.exists()) {
                    println("Утилита не найдена по пути: $toolName")
                    continue
                }

                println("Пробуем утилиту: $toolName")

                try {
                    val processBuilder = ProcessBuilder(tool)
                    processBuilder.redirectErrorStream(true)

                    val process = processBuilder.start()

                    val output = try {
                        process.inputStream.bufferedReader().use { it.readText() }
                    } catch (e: Exception) {
                        "Не удалось прочитать вывод"
                    }

                    val finished = process.waitFor(30, TimeUnit.SECONDS)

                    if (finished && process.exitValue() == 0) {
                        println("✅ Утилита $toolName успешно извлекла архив")
                        extractionSuccess = true
                        usedTool = toolName
                        break
                    } else {
                        if (!finished) {
                            println("$toolName превысил таймаут")
                            process.destroy()
                        } else {
                            println("$toolName вернул код ошибки: ${process.exitValue()}")
                        }
                        if (output.isNotEmpty()) {
                            println("Вывод утилиты: ${output.take(500)}...")
                        }
                    }
                } catch (e: IOException) {
                    println("Не удалось запустить $toolName: ${e.message}")
                } catch (e: Exception) {
                    println("Ошибка при работе с $toolName: ${e.message}")
                }
            }

            if (!extractionSuccess) {
                println("Ни одна утилита не смогла извлечь RAR архив")
                showInstallationInstructions()
                tempDir.deleteRecursively()
                return emptyList()
            }

            println("Архив успешно извлечен с помощью: $usedTool")

            // Проверяем что в временной директории есть файлы
            val allFiles = tempDir.listFiles()
            if (allFiles == null || allFiles.isEmpty()) {
                println("⚠️ Временная директория пуста после извлечения")
                tempDir.deleteRecursively()
                return emptyList()
            }

            println("Содержимое временной директории:")
            tempDir.walk().forEach { file ->
                if (file.isFile) {
                    println("   - ${file.name} (${file.length()} bytes)")
                }
            }

            // Получаем ВСЕ файлы из временной директории
            val allFilesInTemp = tempDir.walk()
                .filter { it.isFile }
                .sortedBy { it.name }
                .toList()

            // Фильтруем только изображения
            var extractedFiles = allFilesInTemp.filter { isImageFile(it.name) }

            if (extractedFiles.isEmpty()) {
                println("⚠В архиве не найдено изображений по расширениям")
                println("Все извлеченные файлы: ${allFilesInTemp.map { it.name }}")

                if (allFilesInTemp.isNotEmpty()) {
                    println("Используем все файлы как изображения")
                    extractedFiles = allFilesInTemp
                }
            }

            println("Найдено файлов для обработки: ${extractedFiles.size}")

            // Читаем файлы
            extractedFiles.forEachIndexed { index, file ->
                progressTracker?.updateProgress(
                    progress = index.toFloat() / extractedFiles.size.toFloat(),
                    operation = "Чтение: ${file.name} (${index + 1}/${extractedFiles.size})"
                )

                try {
                    val data = file.readBytes()
                    if (data.isNotEmpty()) {
                        entries.add(ArchiveEntry(file.name, data, rarFile.absolutePath))
                        println("Прочитан: ${file.name} (${data.size} bytes)")
                    } else {
                        println("Пустой файл: ${file.name}")
                    }
                } catch (e: Exception) {
                    println("Ошибка чтения ${file.name}: ${e.message}")
                }
            }

            // Очистка
            try {
                tempDir.deleteRecursively()
                println("Временная директория очищена")
            } catch (e: Exception) {
                println("Не удалось очистить временную директорию: ${e.message}")
            }

            println("Успешно извлечено ${entries.size} файлов из RAR архива")

        } catch (e: Exception) {
            println("Критическая ошибка при извлечении RAR: ${e.message}")
            e.printStackTrace()
        }

        return entries
    }

    actual override suspend fun createArchive(entries: List<ArchiveEntry>, outputPath: String, progressTracker: MergeProgressTracker?): Boolean =
        withContext(Dispatchers.IO) {
            try {
                createZipArchive(entries, outputPath, progressTracker)
            } catch (e: Exception) {
                println("Ошибка при создании архива: ${e.message}")
                false
            }
        }

    private suspend fun extractZipFiles(zipPath: String, progressTracker: MergeProgressTracker?): List<ArchiveEntry> {
        val entries = mutableListOf<ArchiveEntry>()
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
        return entries
    }

    private fun showInstallationInstructions() {
        println("\nИНСТРУКЦИЯ ПО УСТАНОВКЕ:")
        println("================================")
        println("Для работы с CBR файлами требуется установить одну из программ:")
        println()
        println("1. 7-Zip (рекомендуется):")
        println("   - Скачайте с https://www.7-zip.org/")
        println("   - Установите в папку по умолчанию")
        println("   - Или добавьте в PATH переменную окружения")
        println()
        println("2. WinRAR:")
        println("   - Скачайте с https://www.win-rar.com/")
        println("   - Установите в папку по умолчанию")
        println()
        println("3. Или добавьте путь к утилите в системную переменную PATH")
        println("================================")
    }

    private suspend fun createZipArchive(entries: List<ArchiveEntry>, outputPath: String, progressTracker: MergeProgressTracker?): Boolean {
        return try {
            FileOutputStream(outputPath).use { fileOutput ->
                ZipOutputStream(BufferedOutputStream(fileOutput)).use { zipOutput ->
                    entries.forEachIndexed { index, entry ->
                        progressTracker?.updateProgress(
                            progress = index.toFloat() / entries.size.toFloat(),
                            operation = "Создание архива (${index + 1}/${entries.size})"
                        )

                        // Нормализуем имя файла для избежания конфликтов
                        val safeName = normalizeFileName(entry.name, index + 1, entries.size)
                        val zipEntry = ZipEntry(safeName)
                        zipOutput.putNextEntry(zipEntry)
                        zipOutput.write(entry.data)
                        zipOutput.closeEntry()
                    }
                }
            }
            progressTracker?.updateProgress(1f, "Архив создан!")
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun normalizeFileName(originalName: String, index: Int, totalFiles: Int): String {
        // Убираем пути из имени файла (на случай если в архиве есть поддиректории)
        val cleanName = originalName.substringAfterLast('/').substringAfterLast('\\')

        // Извлекаем расширение файла
        val extension = cleanName.substringAfterLast('.', "").lowercase()

        // Определяем формат номера в зависимости от общего количества файлов
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