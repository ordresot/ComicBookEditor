package com.ordresot.cbeditor.presentation.ui.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ordresot.cbeditor.ui.merger.SelectedFilesList

@Composable
fun FileDropArea(
    selectedFiles: List<String>,
    onFilesSelected: (List<String>) -> Unit,
    onPickFiles: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isDragOver by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .border(
                width = 2.dp,
                color = when {
                    isLoading -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    isDragOver -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = when {
                    isLoading -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    isDragOver -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                },
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
                            "Перетащите CBR/CBZ файлы сюда или кликните для выбора"
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