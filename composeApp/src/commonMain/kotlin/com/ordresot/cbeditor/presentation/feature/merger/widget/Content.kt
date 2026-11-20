package com.ordresot.cbeditor.presentation.feature.merger.widget

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
import com.ordresot.cbeditor.presentation.feature.merger.MergerIntent
import com.ordresot.cbeditor.presentation.feature.merger.MergerState

@Composable
fun Content(
    state: MergerState,
    onIntent: (MergerIntent) -> Unit
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
            message = state.message,
            onDismiss = { onIntent(MergerIntent.OnDismissSuccess) },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        MergeProgress(
            progress = state.progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        FileDropArea(
            selectedFiles = state.selectedFiles,
            onFilesSelected = { files ->
                onIntent(MergerIntent.OnFilesSelected(files))
            },
            onPickFiles = { onIntent(MergerIntent.OnShowFileDialog) },
            isLoading = state.isLoading,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        if (state.selectedFiles.isNotEmpty()) {
            OutputFileNameField(
                fileName = state.outputFileName,
                onFileNameChange = { fileName ->
                    onIntent(MergerIntent.OnOutputFileNameChanged(fileName))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }

        Button(
            onClick = { onIntent(MergerIntent.OnMergeFiles) },
            enabled = state.selectedFiles.size >= 2 && !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Объединить файлы (${state.selectedFiles.size})",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    FileDialog(
        isShowDialog = state.isShowFileDialog,
        currentDirectory = state.dialogCurrentDirectory,
        selectedFiles = state.selectedFiles,
        pathText = state.dialogPathText,
        onNavigateUp = { onIntent(MergerIntent.OnNavigateUp) },
        onNavigateToDirectory = { onIntent(MergerIntent.OnNavigateToDirectory(it)) },
        onNavigateToCustomPath = { onIntent(MergerIntent.OnNavigateToCustomPath(it)) },
        onToggleFileSelection = { onIntent(MergerIntent.OnToggleFileSelection(it)) },
        onSelectAll = { onIntent(MergerIntent.OnSelectAll) },
        onDismiss = { onIntent(MergerIntent.OnDismissFileDialog) },
        onClearSelection = { onIntent(MergerIntent.OnClearSelection) },
        onUpdatePathText = { onIntent(MergerIntent.OnUpdateDialogPathText(it)) }
    )
}