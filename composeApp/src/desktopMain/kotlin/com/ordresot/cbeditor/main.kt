package com.ordresot.cbeditor

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.ordresot.cbeditor.presentation.ui.ComicEditorApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Comic Book Editor",
        state = rememberWindowState().apply {
            placement = WindowPlacement.Maximized
        }
    ) {
        MaterialTheme(
            colorScheme = lightColorScheme()
        ) {
            ComicEditorApp(Platform.Desktop)
        }
    }
}