package com.ordresot.cbeditor.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.ordresot.cbeditor.Platform
import com.ordresot.cbeditor.presentation.core.ui.NavigationBar
import com.ordresot.cbeditor.presentation.feature.converter.ConvertFileScreen
import com.ordresot.cbeditor.presentation.feature.editor.EditFileScreen
import com.ordresot.cbeditor.presentation.feature.main.MainScreen
import com.ordresot.cbeditor.presentation.feature.merger.MergerScreen

/**
 * Корневой навигационный граф приложения.
 * Использует Voyager Navigator для переключения между основными экранами.
 */
@Composable
fun RootNavGraph(
    platform: Platform
) {
    var currentRoute by remember { mutableStateOf<ScreenRoute>(ScreenRoute.Main) }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            if (platform is Platform.Desktop) {
                NavigationBar(currentRoute) { route -> currentRoute = route }
            }
        },
        bottomBar = {
            if (platform is Platform.Android) {
                NavigationBar(currentRoute) { route -> currentRoute = route }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Navigator(screen = MainScreen()) { navigator ->
                LocalNavigatorProvider(navigator) {
                    when (currentRoute) {
                        ScreenRoute.Main -> MainScreen().Content()
                        ScreenRoute.Editor -> EditFileScreen().Content()
                        ScreenRoute.Merger -> MergerScreen().Content()
                        ScreenRoute.Converter -> ConvertFileScreen().Content()
                    }
                }
            }
        }
    }
}

/**
 * CompositionLocal провайдер для Navigator.
 * Позволяет экранам получать доступ к Navigator через LocalNavigator.
 */
@Composable
fun LocalNavigatorProvider(
    navigator: Navigator,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalNavigator provides navigator
    ) {
        content()
    }
}

/**
 * CompositionLocal для доступа к Navigator из экранов.
 */
val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No Navigator provided")
}
