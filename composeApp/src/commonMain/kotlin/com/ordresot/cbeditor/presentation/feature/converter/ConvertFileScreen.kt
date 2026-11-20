package com.ordresot.cbeditor.presentation.feature.converter

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
 * Voyager Screen для экрана конвертации файла (stub).
 */
class ConvertFileScreen : Screen {
    @Composable
    override fun Content() {
        ConvertFileContent()
    }
}

/**
 * Pure Compose UI экрана конвертации.
 */
@Composable
fun ConvertFileContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Экран конвертации - в разработке",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
