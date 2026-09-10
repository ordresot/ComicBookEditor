package com.ordresot.cbeditor.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import com.ordresot.cbeditor.data.repository.PreferencesRepositoryImpl
import java.io.File

@Composable
fun CustomFileDialog(
    onFilesSelected: (List<File>) -> Unit,
    onDismiss: () -> Unit
) {
    val preferencesRepositoryImpl = remember { PreferencesRepositoryImpl() }

    var currentDirectory by remember {
        mutableStateOf(preferencesRepositoryImpl.getDefaultDirectory())
    }
    var selectedFiles by remember { mutableStateOf<Set<File>>(emptySet()) }
    var pathText by remember { mutableStateOf(currentDirectory.absolutePath) }

    // Обновляем путь когда меняется директория
    LaunchedEffect(currentDirectory) {
        pathText = currentDirectory.absolutePath
        // Сохраняем новую директорию при каждом изменении
        preferencesRepositoryImpl.saveLastDirectory(currentDirectory)
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
                    .padding(24.dp) // Увеличили отступы для большего размера
            ) {
                // Панель пути с кнопкой домашней директории
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Кнопка домашней директории
                    IconButton(
                        onClick = {
                            currentDirectory = File(System.getProperty("user.home"))
                        }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.Home),
                            contentDescription = "Домашняя папка",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(Modifier.width(8.dp))

                    // Кнопка назад
                    IconButton(
                        onClick = {
                            currentDirectory.parentFile?.let { currentDirectory = it }
                        },
                        enabled = currentDirectory.parentFile != null
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
                        onValueChange = { pathText = it },
                        placeholder = { Text("Путь к папке") },
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        trailingIcon = {
                            Row {
                                // Кнопка обновления
                                IconButton(
                                    onClick = {
                                        // Обновляем список файлов
                                        val dir = File(pathText)
                                        if (dir.exists() && dir.isDirectory) {
                                            currentDirectory = dir
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = rememberVectorPainter(Icons.Default.Refresh),
                                        contentDescription = "Обновить",
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(Modifier.width(4.dp))

                                // Кнопка перехода
                                IconButton(
                                    onClick = {
                                        val newDir = File(pathText)
                                        if (newDir.exists() && newDir.isDirectory) {
                                            currentDirectory = newDir
                                        }
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

                // Информация о текущей директории
                CurrentDirectoryInfo(
                    directory = currentDirectory,
                    selectedCount = selectedFiles.size,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Список файлов и папок
                val files by remember(currentDirectory) {
                    derivedStateOf {
                        currentDirectory.listFiles()?.filter {
                            it.isDirectory || isComicFile(it)
                        }?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
                            ?: emptyList()
                    }
                }

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(files) { file ->
                        FileListItem(
                            file = file,
                            isSelected = selectedFiles.contains(file),
                            onClick = {
                                if (file.isDirectory) {
                                    currentDirectory = file
                                } else {
                                    selectedFiles = if (selectedFiles.contains(file)) {
                                        selectedFiles - file
                                    } else {
                                        selectedFiles + file
                                    }
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

                // Панель действий
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
                        onClick = {
                            onFilesSelected(selectedFiles.toList())
                            // Сохраняем текущую директорию при подтверждении выбора
                            preferencesRepositoryImpl.saveLastDirectory(currentDirectory)
                        },
                        enabled = selectedFiles.isNotEmpty(),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Выбрать", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun FileListItem(
    file: File,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val fileType = when {
        file.isDirectory -> "Папка"
        file.extension.lowercase() == "cbz" -> "CBZ"
        file.extension.lowercase() == "cbr" -> "CBR"
        else -> "Файл"
    }

    val icon = when {
        file.isDirectory -> Icons.Default.Folder
        file.extension.lowercase() == "cbz" -> Icons.Default.Archive
        file.extension.lowercase() == "cbr" -> Icons.Default.Archive
        else -> Icons.Default.PictureAsPdf
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = rememberVectorPainter(icon),
            contentDescription = fileType,
            modifier = Modifier.size(28.dp)
        )

        Spacer(Modifier.width(20.dp))

        Text(
            text = file.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        // Информация о типе файла
        Text(
            text = fileType,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        if (isSelected) {
            Icon(
                painter = rememberVectorPainter(Icons.Default.Check),
                contentDescription = "Выбрано",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun CurrentDirectoryInfo(
    directory: File,
    selectedCount: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Текущая папка:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = directory.absolutePath,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "Выбрано: $selectedCount",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun isComicFile(file: File): Boolean {
    return file.name.lowercase().let {
        it.endsWith(".cbr") || it.endsWith(".cbz")
    }
}