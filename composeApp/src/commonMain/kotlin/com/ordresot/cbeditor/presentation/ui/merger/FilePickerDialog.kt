package com.ordresot.cbeditor.presentation.ui.merger

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun FilePickerDialog(
    onDismiss: () -> Unit,
    onFilesSelected: (List<String>) -> Unit
) {
    // TODO: реализовать platform-specific диалог выбора файлов
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выбор файлов") },
        text = { Text("Диалог выбора файлов будет реализован для каждой платформы отдельно") },
        confirmButton = {
            Button(onClick = {
                // Временная заглушка - добавляем тестовые файлы
                onFilesSelected(listOf("file1.cbz", "file2.cbr", "file3.cbz"))
            }) {
                Text("Выбрать тестовые файлы")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}