package com.ordresot.cbeditor.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ordresot.cbeditor.Platform
import com.ordresot.cbeditor.presentation.ui.components.NavigationBar

@Composable
fun RootNavGraph(
    startDestination: NavigationEffect,
    platform: Platform
) {
    val navController = rememberNavController()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            if (platform is Platform.Desktop) {
                NavigationBar(currentScreen, onScreenChange = { currentScreen = it })
            }
        },
        bottomBar = {
            if (platform is Platform.Android) {
                NavigationBar(currentScreen, onScreenChange = { currentScreen = it })
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            composableWithoutAnimation<NavigationEffect.NavigateToMainScreen> {

            }
        }
    }
}