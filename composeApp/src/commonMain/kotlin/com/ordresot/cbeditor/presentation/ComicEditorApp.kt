package com.ordresot.cbeditor.presentation

import androidx.compose.runtime.Composable
import com.ordresot.cbeditor.Platform
import com.ordresot.cbeditor.presentation.navigation.NavigationEffect
import com.ordresot.cbeditor.presentation.navigation.RootNavGraph

@Composable
fun ComicEditorApp(
    platform: Platform
) {
    RootNavGraph(
        startDestination = NavigationEffect.NavigateToMainScreen,
        platform = platform
    )
}
