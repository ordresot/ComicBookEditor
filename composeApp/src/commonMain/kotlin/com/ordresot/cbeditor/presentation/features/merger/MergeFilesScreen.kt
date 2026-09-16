package com.ordresot.cbeditor.presentation.features.merger

import androidx.compose.runtime.Composable
import com.ordresot.cbeditor.presentation.navigation.NavigationEffect
import com.ordresot.cbeditor.presentation.core.base.BaseScreen
import com.ordresot.cbeditor.presentation.features.merger.widget.Content
import org.koin.compose.koinInject

@Composable
fun MergeFilesScreen(
    viewModel: MergerViewModel = koinInject(),
    onNavigation: (NavigationEffect) -> Unit,
) = BaseScreen(viewModel, onNavigation) { uiState, onAction ->
    when (uiState) {
        is MergerUiState.Content -> Content(uiState, onAction)
    }
}

