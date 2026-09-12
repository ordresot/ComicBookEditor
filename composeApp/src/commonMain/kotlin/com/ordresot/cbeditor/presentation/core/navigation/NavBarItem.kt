package com.ordresot.cbeditor.presentation.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Merge
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavBarItem(
    val route: String,
    val icon: ImageVector,
    val contentDescription: String,
) {
    object Main : NavBarItem(
        route = NavigationEffect.NavigateToMainScreen::class.qualifiedName ?: "",
        icon = Icons.Default.Home,
        contentDescription = "Главный экран",
    )
    object Editor : NavBarItem(
        route = NavigationEffect.NavigateToEditorScreen::class.qualifiedName ?: "",
        icon = Icons.Default.Edit,
        contentDescription = "Экран редактирования",
    )
    object Merger : NavBarItem(
        route = NavigationEffect.NavigateToMergerScreen::class.qualifiedName ?: "",
        icon = Icons.Default.Merge,
        contentDescription = "Экран объединения",
    )
    object Converter : NavBarItem(
        route = NavigationEffect.NavigateToConverterScreen::class.qualifiedName ?: "",
        icon = Icons.Default.SwapHoriz,
        contentDescription = "Экран конвертации",
    )

    companion object {
        val items = listOf(Main, Editor, Merger, Converter)
    }
}