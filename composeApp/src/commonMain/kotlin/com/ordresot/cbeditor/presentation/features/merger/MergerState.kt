package com.ordresot.cbeditor.presentation.features.merger

import com.ordresot.cbeditor.presentation.features.merger.uiState.MessageUi
import com.ordresot.cbeditor.presentation.features.merger.uiState.ProgressUi
import java.io.File

data class MergerState(
    val selectedFiles: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val message: MessageUi? = null,
    val outputFileName: String = "",
    val progress: ProgressUi? = null,
    val isShowFileDialog: Boolean = false,
    val dialogCurrentDirectory: String = "",
    val dialogPathText: String = ""
) {
    fun stateSelectedFiles(files: List<String>): MergerState = copy(selectedFiles = files)

    fun stateOutputFileName(fileName: String): MergerState = copy(outputFileName = fileName)

    fun stateShowMessage(text: String, type: MessageUi.MessageType): MergerState =
        copy(message = MessageUi(text, type))

    fun stateDismissMessage(): MergerState = copy(message = null)

    fun stateProgressUpdate(progress: Float, operation: String): MergerState =
        copy(progress = ProgressUi(progress, operation))

    fun stateMergingStarted(): MergerState =
        copy(isLoading = true, message = null)

    fun stateMergeSuccess(outputPath: String): MergerState =
        copy(
            isLoading = false,
            message = MessageUi("Файлы успешно объединены в: ${File(outputPath).name}", MessageUi.MessageType.Success),
            selectedFiles = emptyList(),
            outputFileName = "",
            progress = null
        )

    fun stateMergeError(message: String): MergerState =
        copy(isLoading = false, message = MessageUi(message, MessageUi.MessageType.Error))

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
