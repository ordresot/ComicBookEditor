package com.ordresot.cbeditor.presentation.features.merger.widget

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ordresot.cbeditor.presentation.core.ui.FileDialog
import com.ordresot.cbeditor.presentation.core.ui.FileDropArea
import com.ordresot.cbeditor.presentation.core.ui.MessageBanner
import com.ordresot.cbeditor.presentation.core.ui.OutputFileNameField
import com.ordresot.cbeditor.presentation.features.merger.MergerAction
import com.ordresot.cbeditor.presentation.features.merger.MergerUiState

@Composable
fun Content(
    uiState: MergerUiState.Content,
    onAction: (MergerAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = "Выберите CBR/CBZ файлы для объединения. Файлы будут объединены в порядке их выбора.",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        MessageBanner(
            message = uiState.message,
            onDismiss = { onAction(MergerAction.OnDismissSuccess) },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        MergeProgress(
            progress = uiState.progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        FileDropArea(
            selectedFiles = uiState.selectedFiles,
            onFilesSelected = { files ->
                onAction(MergerAction.OnFilesSelected(files))
            },
            onPickFiles = { onAction(MergerAction.OnShowFileDialog) },
            isLoading = uiState.isLoading,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

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
        }
        
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

    FileDialog(
        isShowDialog = uiState.isShowFileDialog,
        currentDirectory = uiState.dialogCurrentDirectory,
        selectedFiles = uiState.selectedFiles,
        pathText = uiState.dialogPathText,
        onNavigateUp = { onAction(MergerAction.OnNavigateUp) },
        onNavigateToDirectory = { onAction(MergerAction.OnNavigateToDirectory(it)) },
        onNavigateToCustomPath = { onAction(MergerAction.OnNavigateToCustomPath(it)) },
        onToggleFileSelection = { onAction(MergerAction.OnToggleFileSelection(it)) },
        onConfirm = { onAction(MergerAction.OnConfirmFileDialog) },
        onDismiss = { onAction(MergerAction.OnDismissFileDialog) },
        onUpdatePathText = { onAction(MergerAction.OnUpdateDialogPathText(it)) }
    )
}
