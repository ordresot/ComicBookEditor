package com.ordresot.cbeditor.presentation.merger

import androidx.lifecycle.viewModelScope
import com.ordresot.cbeditor.presentation.core.navigation.NavigationEffect
import com.ordresot.cbeditor.presentation.core.base.BaseViewModel
import com.ordresot.cbeditor.domain.repository.FileRepository
import com.ordresot.cbeditor.presentation.merger.widget.MergerConverter
import com.ordresot.cbeditor.utils.MergeProgressTracker
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class MergerViewModel : BaseViewModel<MergerState, MergerUiState, MergerAction, NavigationEffect>(
    converter = MergerConverter(),
    initState = MergerState()
), KoinComponent {
    private val fileRepository: FileRepository by inject()

    override fun onAction(action: MergerAction) {
        when (action) {
            is MergerAction.OnFilesSelected -> {
                updateState { stateSelectedFiles(action.files) }
                if (action.files.isNotEmpty()) {
                    val firstFileName = File(action.files.first()).nameWithoutExtension
                    updateState { stateOutputFileName(firstFileName) }
                } else {
                    updateState { stateOutputFileName("") }
                }
            }
            is MergerAction.OnOutputFileNameChanged -> {
                updateState { stateOutputFileName(action.fileName) }
            }
            is MergerAction.OnMergeFiles -> mergeFiles()
            is MergerAction.OnDismissError -> {
                updateState { stateDismissError() }
            }
            is MergerAction.OnDismissSuccess -> {
                updateState { stateDismissSuccess() }
            }
            is MergerAction.OnProgressUpdate -> {
                updateState { stateProgressUpdate(action.progress, action.operation) }
            }
        }
    }

    private fun mergeFiles() {
        val state = state
        if (state.selectedFiles.size < 2) return

        viewModelScope.launch {
            updateState { stateMergingStarted() }

            try {
                val progressTracker = MergeProgressTracker()
                val outputPath = generateOutputPath(state.selectedFiles, state.outputFileName)

                val success = fileRepository.mergeFiles(
                    sourcePaths = state.selectedFiles,
                    targetPath = outputPath,
                    progressTracker = progressTracker
                )

                if (success) {
                    updateState { stateMergeSuccess(outputPath) }
                } else {
                    updateState { stateMergeError("Ошибка при объединении файлов") }
                }
            } catch (e: Exception) {
                updateState { stateMergeError("Ошибка: ${e.message}") }
            }
        }
    }

    private fun generateOutputPath(sourcePaths: List<String>, customFileName: String): String {
        val firstFile = File(sourcePaths.first())
        val parentDir = firstFile.parent ?: System.getProperty("user.home")
        val fileName = customFileName.ifEmpty { firstFile.nameWithoutExtension }
        return "$parentDir/${fileName}.cbz"
    }
}
