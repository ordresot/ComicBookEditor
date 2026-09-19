package com.ordresot.cbeditor

import androidx.compose.ui.window.Window
import com.ordresot.cbeditor.presentation.ComicEditorApp
import com.ordresot.cbeditor.presentation.theme.ComicEditorTheme
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.ordresot.cbeditor.presentation.ComicEditorApp

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Comic Book Editor",
        state = rememberWindowState().apply {
            placement = WindowPlacement.Maximized
        }
    ) {
        ComicEditorTheme {
            ComicEditorApp(Platform.Desktop)
        }
    }
}
