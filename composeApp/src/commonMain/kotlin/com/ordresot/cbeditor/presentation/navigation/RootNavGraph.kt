package com.ordresot.cbeditor.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ordresot.cbeditor.Platform
import com.ordresot.cbeditor.presentation.features.converter.ConvertFileScreen
import com.ordresot.cbeditor.presentation.features.editor.EditFileScreen
import com.ordresot.cbeditor.presentation.features.main.MainScreen
import com.ordresot.cbeditor.presentation.features.merger.MergeFilesScreen
import com.ordresot.cbeditor.presentation.features.merger.MergerViewModel
import com.ordresot.cbeditor.presentation.core.ui.NavigationBar
import org.koin.compose.koinInject

@Composable
fun RootNavGraph(
    startDestination: NavigationEffect,
    platform: Platform
) {
    var currentScreen by remember { mutableStateOf(startDestination) }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            if (platform is Platform.Desktop) {
                NavigationBar(currentScreen) { currentScreen = it }
            }
        },
        bottomBar = {
            if (platform is Platform.Android) {
                NavigationBar(currentScreen) { currentScreen = it }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                NavigationEffect.NavigateToMainScreen -> MainScreen()
                NavigationEffect.NavigateToEditorScreen -> EditFileScreen()
                NavigationEffect.NavigateToMergerScreen -> {
                    val viewModel: MergerViewModel = koinInject()
                    MergeFilesScreen(
                        viewModel = viewModel,
                        onNavigation = { }
                    )
                }
                NavigationEffect.NavigateToConverterScreen -> ConvertFileScreen()
            }
        }
    }
}
