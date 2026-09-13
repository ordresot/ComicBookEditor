package com.ordresot.cbeditor.presentation.features.merger

import com.ordresot.cbeditor.presentation.core.base.StateConverter

class MergerConverter : StateConverter<MergerState, MergerUiState> {
    override fun uiMap(state: MergerState): MergerUiState = with(state) {
        MergerUiState.Content(
            selectedFiles = state.selectedFiles,
            isLoading = state.isLoading,
            message = state.message,
            outputFileName = state.outputFileName,
            progress = state.progress,
            isShowFileDialog = state.isShowFileDialog,
            dialogCurrentDirectory = state.dialogCurrentDirectory,
            dialogPathText = state.dialogPathText,
        )
    }
}
