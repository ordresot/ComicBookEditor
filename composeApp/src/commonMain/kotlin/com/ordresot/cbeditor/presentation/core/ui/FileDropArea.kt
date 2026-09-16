package com.ordresot.cbeditor.presentation.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ordresot.cbeditor.presentation.features.merger.widget.SelectedFilesList

@Composable
fun FileDropArea(
    selectedFiles: List<String>,
    onFilesSelected: (List<String>) -> Unit,
    onPickFiles: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 2.dp,
                color = if (isLoading) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = if (isLoading) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = !isLoading) {
                onPickFiles()
            }
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 3.dp
                    )
                    Text(
                        text = "Загрузка файлов...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.FileUpload),
                        contentDescription = "Загрузить файлы",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(64.dp)
                    )

                    Text(
                        text = if (selectedFiles.isEmpty()) {
                            "Выберите CBR/CBZ файлы"
                        } else {
                            "Выбрано файлов: ${selectedFiles.size}\nКликните для добавления еще"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    if (selectedFiles.isNotEmpty()) {
                        SelectedFilesList(
                            files = selectedFiles,
                            onRemoveFile = { fileToRemove ->
                                onFilesSelected(selectedFiles - fileToRemove)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
