package com.ordresot.cbeditor.presentation.feature.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

/**
 * Voyager Screen для экрана редактирования файла (stub).
 */
class EditFileScreen : Screen {
    @Composable
    override fun Content() {
        EditFileContent()
    }
}

/**
 * Pure Compose UI экрана редактирования.
 */
@Composable
fun EditFileContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Экран редактирования файла - в разработке",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
