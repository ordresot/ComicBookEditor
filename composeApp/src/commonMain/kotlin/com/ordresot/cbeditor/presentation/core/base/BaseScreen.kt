package com.ordresot.cbeditor.presentation.core.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
inline fun <reified UI_STATE, reified ACTION, reified EFFECT> BaseScreen(
    viewModel: BaseViewModel<*, UI_STATE, ACTION, EFFECT>,
    crossinline onNavigation: (EFFECT) -> Unit,
    crossinline content: @Composable (uiState: UI_STATE, onAction: (ACTION) -> Unit) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val onAction = viewModel::onAction
    LaunchedEffect(Unit) {
        viewModel.uiEffects.collect { onNavigation(it) }
    }
    content(uiState, onAction)
}
