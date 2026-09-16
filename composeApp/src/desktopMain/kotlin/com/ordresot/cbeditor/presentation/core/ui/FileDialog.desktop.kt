package com.ordresot.cbeditor.presentation.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import java.io.File

@Composable
actual fun FileDialog(
    isShowDialog: Boolean,
    currentDirectory: String,
    selectedFiles: List<String>,
    pathText: String,
    onNavigateUp: () -> Unit,
    onNavigateToDirectory: (String) -> Unit,
    onNavigateToCustomPath: (String) -> Unit,
    onToggleFileSelection: (String) -> Unit,
    onSelectAll: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onUpdatePathText: (String) -> Unit
) {
    if (!isShowDialog) return

    val currentDirFile = remember(currentDirectory) { File(currentDirectory) }

    val files by remember(currentDirectory) {
        derivedStateOf {
            currentDirFile.listFiles()?.filter {
                it.isDirectory || isComicFile(it)
            }?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
                ?: emptyList()
        }
    }

    DialogWindow(
        onCloseRequest = onDismiss,
        title = "Выберите файлы комиксов",
        state = rememberDialogState(
            width = 1200.dp,
            height = 800.dp
        ),
        resizable = true,
        alwaysOnTop = true
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .shadow(16.dp, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Панель пути
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onNavigateToDirectory(File(System.getProperty("user.home")).absolutePath) }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.Home),
                            contentDescription = "Домашняя папка",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    IconButton(
                        onClick = onNavigateUp,
                        enabled = File(currentDirectory).parentFile != null
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.ArrowBack),
                            contentDescription = "Назад",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    OutlinedTextField(
                        value = pathText,
                        onValueChange = onUpdatePathText,
                        placeholder = { Text("Путь к папке") },
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        trailingIcon = {
                            Row {
                                IconButton(
                                    onClick = {
                                        onNavigateToCustomPath(pathText)
                                    }
                                ) {
                                    Icon(
                                        painter = rememberVectorPainter(Icons.Default.Refresh),
                                        contentDescription = "Обновить",
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(Modifier.width(4.dp))

                                IconButton(
                                    onClick = {
                                        onNavigateToCustomPath(pathText)
                                    }
                                ) {
                                    Icon(
                                        painter = rememberVectorPainter(Icons.Default.Search),
                                        contentDescription = "Перейти",
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    )
                }

                Spacer(Modifier.height(20.dp))

                CurrentDirectoryInfo(
                    directory = currentDirFile,
                    selectedCount = selectedFiles.size,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(files) { file ->
                        val isSelected = selectedFiles.contains(file.absolutePath)
                        val selectedPosition = if (isSelected) {
                            selectedFiles.indexOf(file.absolutePath) + 1
                        } else {
                            null
                        }
                        FileListItem(
                            file = file,
                            isSelected = isSelected,
                            selectedPosition = selectedPosition,
                            onClick = {
                                if (file.isDirectory) {
                                    onNavigateToDirectory(file.absolutePath)
                                } else {
                                    onToggleFileSelection(file.absolutePath)
                                }
                            }
                        )

                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedFiles.isNotEmpty()) "Выбрано: ${selectedFiles.size}" else "Файлы не выбраны",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(20.dp))

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Отмена", style = MaterialTheme.typography.bodyLarge)
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = onSelectAll,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Выбрать все", style = MaterialTheme.typography.bodyLarge)
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = onConfirm,
                        enabled = selectedFiles.isNotEmpty(),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Подтвердить", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

private fun isComicFile(file: File): Boolean {
    return file.name.lowercase().let {
        it.endsWith(".cbr") || it.endsWith(".cbz")
    }
}
