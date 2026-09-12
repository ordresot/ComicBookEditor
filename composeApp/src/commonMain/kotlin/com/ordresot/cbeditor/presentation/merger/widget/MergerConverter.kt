package com.ordresot.cbeditor.presentation.merger.widget

import com.ordresot.cbeditor.presentation.core.base.StateConverter
import com.ordresot.cbeditor.presentation.merger.MergerState
import com.ordresot.cbeditor.presentation.merger.MergerUiState

class MergerConverter : StateConverter<MergerState, MergerUiState> {
    override fun uiMap(state: MergerState) = MergerUiState.Content(
    selectedFiles = state.selectedFiles,
    isLoading = state.isLoading,
    errorMessage = state.errorMessage,
    successMessage = state.successMessage,
    outputFileName = state.outputFileName,
    progress = state.progress,
    currentOperation = state.currentOperation
    )
}