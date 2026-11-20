package com.ordresot.cbeditor.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.navigator.tab.Tab

/**
 * Данные навигационного таба.
 */
data class NavTab(
    val title: String,
    val icon: ImageVector,
    val route: ScreenRoute
)
