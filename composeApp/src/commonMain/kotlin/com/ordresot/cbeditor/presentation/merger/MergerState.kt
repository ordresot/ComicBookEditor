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

    fun stateSelectedFiles(files: List<String>): MergerState = copy(selectedFiles = files)

    fun stateOutputFileName(fileName: String): MergerState = copy(outputFileName = fileName)

    fun stateDismissError(): MergerState = copy(errorMessage = "")

    fun stateDismissSuccess(): MergerState = copy(successMessage = "")

    fun stateProgressUpdate(progress: Float, operation: String): MergerState =
        copy(progress = progress, currentOperation = operation)

    fun stateMergingStarted(): MergerState =
        copy(isLoading = true, errorMessage = "", successMessage = "")

    fun stateMergeSuccess(outputPath: String): MergerState =
        copy(
            isLoading = false,
            successMessage = "Файлы успешно объединены в: ${java.io.File(outputPath).name}",
            selectedFiles = emptyList(),
            outputFileName = ""
        )

    fun stateMergeError(message: String): MergerState =
        copy(isLoading = false, errorMessage = message)
}