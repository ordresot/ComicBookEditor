package com.ordresot.cbeditor.presentation.features.merger

import java.io.File

data class MergerState(
    val selectedFiles: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = "",
    val outputFileName: String = "",
    val progress: Float = 0f,
    val currentOperation: String = "",
    val isShowFileDialog: Boolean = false,
    val dialogCurrentDirectory: String = "",
    val dialogPathText: String = ""
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
            successMessage = "Файлы успешно объединены в: ${File(outputPath).name}",
            selectedFiles = emptyList(),
            outputFileName = "",
            progress = 0f,
            currentOperation = ""
        )

    fun stateMergeError(message: String): MergerState =
        copy(isLoading = false, errorMessage = message)

    fun stateShowFileDialog(): MergerState = copy(isShowFileDialog = true)

    fun stateDismissFileDialog(): MergerState = copy(isShowFileDialog = false)

    fun stateNavigateToDirectory(path: String): MergerState =
        copy(dialogCurrentDirectory = path, dialogPathText = path)

    fun stateNavigateUp(): MergerState = copy(
        dialogCurrentDirectory = File(dialogCurrentDirectory).parent ?: "",
        dialogPathText = File(dialogCurrentDirectory).parent ?: ""
    )

    fun stateNavigateToCustomPath(path: String): MergerState =
        copy(dialogCurrentDirectory = path, dialogPathText = path)

    fun stateToggleFileSelection(path: String): MergerState = copy(
        selectedFiles = if (selectedFiles.contains(path)) {
            selectedFiles - path
        } else {
            selectedFiles + path
        }
    )

    fun stateConfirmFileDialog(): MergerState = copy(
        isShowFileDialog = false
    )

    fun stateUpdateDialogPathText(path: String): MergerState = copy(dialogPathText = path)
}