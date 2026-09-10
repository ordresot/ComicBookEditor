package com.ordresot.cbeditor.presentation.ui.merger

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ordresot.cbeditor.data.repository.FileRepositoryImpl
import com.ordresot.cbeditor.ui.components.CustomFileDialog
import com.ordresot.cbeditor.ui.components.ErrorMessage
import com.ordresot.cbeditor.ui.components.FileDropArea
import com.ordresot.cbeditor.ui.components.OutputFileNameField
import com.ordresot.cbeditor.ui.components.SuccessMessage
import com.ordresot.cbeditor.utils.MergeProgressTracker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun MergeFilesScreen() {
    var selectedFiles by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var showFileDialog by remember { mutableStateOf(false) }
    var outputFileName by remember { mutableStateOf("") }

    // Используем remember для сохранения состояния прогресса
    val progressTracker = remember { MergeProgressTracker() }

    val repository = remember { FileRepositoryImpl() }
    val coroutineScope = rememberCoroutineScope()

    // Автоматически генерируем имя файла когда выбираются файлы
    LaunchedEffect(selectedFiles) {
        if (selectedFiles.isNotEmpty()) {
            val firstFileName = File(selectedFiles.first()).nameWithoutExtension
            outputFileName = firstFileName
        } else {
            outputFileName = ""
        }
    }

    // Обработчик открытия диалога
    val onPickFiles = {
        showFileDialog = true // ← Показываем наш кастомный диалог
    }

    // Обработчик выбора файлов из диалога
    val onFilesSelectedFromDialog = { files: List<File> ->
        selectedFiles = files.map { it.absolutePath }
        errorMessage = null
        showFileDialog = false // ← Закрываем диалог
    }

    // Обработчик отмены диалога
    val onDialogDismiss = {
        showFileDialog = false // ← Просто закрываем диалог
    }

    // Обработчик объединения файлов
    val onMergeFiles: () -> Unit = {
        coroutineScope.launch {
            isLoading = true
            progressTracker.reset()

            try {
                val outputPath = generateOutputPath(selectedFiles, outputFileName)

                // Запускаем объединение с передачей трекера прогресса
                val success = repository.mergeFiles(selectedFiles, outputPath, progressTracker)

                if (success) {
                    successMessage = "Файлы успешно объединены в: ${File(outputPath).name}"
                    selectedFiles = emptyList()

                    // Автоматически скрываем успешное сообщение через 5 секунд
                    coroutineScope.launch {
                        delay(5000)
                        successMessage = null
                    }
                } else {
                    errorMessage = "Ошибка при объединении файлов"
                }
            } catch (e: Exception) {
                errorMessage = "Ошибка: ${e.message}"
            } finally {
                isLoading = false
                // Не сбрасываем прогресс сразу, чтобы пользователь увидел завершение
                coroutineScope.launch {
                    delay(2000)
                    progressTracker.reset()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
    ) {
        // Описание
        Text(
            text = "Выберите CBR/CBZ файлы для объединения. Файлы будут объединены в порядке их выбора.",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Сообщения об ошибках/успехе
        successMessage?.let { message ->
            SuccessMessage(
                message = message,
                onDismiss = { successMessage = null },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        errorMessage?.let { message ->
            ErrorMessage(
                message = message,
                onDismiss = { errorMessage = null },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Прогресс объединения (показываем всегда когда есть операция)
        if (progressTracker.currentOperation.isNotEmpty()) {
            MergeProgress(
                progress = progressTracker.currentProgress,
                currentOperation = progressTracker.currentOperation,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        // Область для Drag'n'Drop
        FileDropArea(
            selectedFiles = selectedFiles,
            onFilesSelected = { files ->
                selectedFiles = files
                errorMessage = null
            },
            onPickFiles = onPickFiles,
            isLoading = isLoading,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Поле для имени выходного файла (показываем только когда есть выбранные файлы)
        if (selectedFiles.isNotEmpty()) {
            OutputFileNameField(
                fileName = outputFileName,
                onFileNameChange = { outputFileName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            Spacer(Modifier.height(24.dp))
        }

        // Кнопка объединения
        Button(
            onClick = onMergeFiles,
            enabled = selectedFiles.size >= 2 && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Объединить файлы (${selectedFiles.size})",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    if (showFileDialog) {
        CustomFileDialog(
            onFilesSelected = onFilesSelectedFromDialog,
            onDismiss = onDialogDismiss
        )
    }
}

private fun generateOutputPath(sourcePaths: List<String>, customFileName: String): String {
    val firstFile = File(sourcePaths.first())
    val parentDir = firstFile.parent ?: System.getProperty("user.home")

    val fileName = customFileName.ifEmpty {
        firstFile.nameWithoutExtension
    }

    return "$parentDir/${fileName}.cbz"
}