package com.ordresot.cbeditor.presentation.merger

data class MergerState(
    val selectedFiles: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = "",
    val outputFileName: String = "",
    val progress: Float = 0f,
    val currentOperation: String = ""
) {

}