package com.ordresot.cbeditor.presentation.features.merger

import com.ordresot.cbeditor.presentation.features.merger.uiState.MessageUi
import com.ordresot.cbeditor.presentation.features.merger.uiState.ProgressUi

sealed interface MergerUiState {
    data class Content(
        val selectedFiles: List<String>,
        val isLoading: Boolean,
        val message: MessageUi?,
        val outputFileName: String,
        val progress: ProgressUi?,
        val isShowFileDialog: Boolean,
        val dialogCurrentDirectory: String,
        val dialogPathText: String,
    ) : MergerUiState
}
