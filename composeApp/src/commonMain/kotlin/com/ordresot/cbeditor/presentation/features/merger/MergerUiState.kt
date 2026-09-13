package com.ordresot.cbeditor.presentation.features.merger

sealed interface MergerUiState {
    data class Content(
        val selectedFiles: List<String>,
        val isLoading: Boolean,
        val errorMessage: String,
        val successMessage: String,
        val outputFileName: String,
        val progress: Float,
        val currentOperation: String,
        val isShowFileDialog: Boolean,
        val dialogCurrentDirectory: String,
        val dialogPathText: String,
    ) : MergerUiState
}