package com.ordresot.cbeditor.presentation.merger.widget

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ordresot.cbeditor.presentation.core.ui.CustomFileDialog
import com.ordresot.cbeditor.presentation.core.ui.FileDropArea
import com.ordresot.cbeditor.presentation.core.ui.OutputFileNameField
import com.ordresot.cbeditor.presentation.core.ui.ErrorMessage
import com.ordresot.cbeditor.presentation.core.ui.SuccessMessage
import com.ordresot.cbeditor.presentation.merger.MergerAction
import com.ordresot.cbeditor.presentation.merger.MergerUiState

@Composable
fun Content(
    uiState: MergerUiState.Content,
    onAction: (MergerAction) -> Unit,
) {
    var showFileDialog by remember { mutableStateOf(false) }

    // Обработчик открытия диалога
    val onPickFiles = {
        showFileDialog = true
    }

    // Обработчик выбора файлов из диалога
    val onFilesSelectedFromDialog = { files: List<java.io.File> ->
        onAction(MergerAction.OnFilesSelected(files.map { it.absolutePath }))
        showFileDialog = false
    }

    // Обработчик отмены диалога
    val onDialogDismiss = {
        showFileDialog = false
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
        uiState.successMessage?.let { message ->
            SuccessMessage(
                message = message,
                onDismiss = { onAction(MergerAction.OnDismissSuccess) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        uiState.errorMessage?.let { message ->
            ErrorMessage(
                message = message,
                onDismiss = { onAction(MergerAction.OnDismissError) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Прогресс объединения
        if (uiState.currentOperation.isNotEmpty()) {
            MergeProgress(
                progress = uiState.progress,
                currentOperation = uiState.currentOperation,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        // Область для Drag'n'Drop
        FileDropArea(
            selectedFiles = uiState.selectedFiles,
            onFilesSelected = { files ->
                onAction(MergerAction.OnFilesSelected(files))
            },
            onPickFiles = onPickFiles,
            isLoading = uiState.isLoading,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Поле для имени выходного файла
        if (uiState.selectedFiles.isNotEmpty()) {
            OutputFileNameField(
                fileName = uiState.outputFileName,
                onFileNameChange = { fileName ->
                    onAction(MergerAction.OnOutputFileNameChanged(fileName))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            Spacer(Modifier.height(24.dp))
        }

        // Кнопка объединения
        Button(
            onClick = { onAction(MergerAction.OnMergeFiles) },
            enabled = uiState.selectedFiles.size >= 2 && !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Объединить файлы (${uiState.selectedFiles.size})",
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
