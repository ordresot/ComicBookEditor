package com.ordresot.cbeditor.presentation.ui

import androidx.compose.runtime.Composable
import com.ordresot.cbeditor.Platform
import com.ordresot.cbeditor.core.navigation.NavigationEffect
import com.ordresot.cbeditor.core.navigation.RootNavGraph
import com.ordresot.cbeditor.getPlatform

@Composable
fun ComicEditorApp(
    platform: Platform
) {
    RootNavGraph(
        startDestination = NavigationEffect.NavigateToMainScreen,
        platform = platform
    )
}