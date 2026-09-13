package com.ordresot.cbeditor.presentation.features.merger

import androidx.lifecycle.viewModelScope
import com.ordresot.cbeditor.presentation.navigation.NavigationEffect
import com.ordresot.cbeditor.presentation.core.base.BaseViewModel
import com.ordresot.cbeditor.domain.repository.FileRepository
import com.ordresot.cbeditor.domain.repository.PreferencesRepository
import com.ordresot.cbeditor.presentation.utils.MergeProgressTracker
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class MergerViewModel : BaseViewModel<MergerState, MergerUiState, MergerAction, NavigationEffect>(
    converter = MergerConverter(),
    initState = MergerState()
), KoinComponent {
    private val fileRepository: FileRepository by inject()
    private val preferencesRepository: PreferencesRepository by inject()

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
                updateState { stateDismissMessage() }
            }
            is MergerAction.OnDismissSuccess -> {
                updateState { stateDismissMessage() }
            }
            is MergerAction.OnShowFileDialog -> {
                val lastDir = preferencesRepository.getLastDirectory()
                val startDir = lastDir?.absolutePath ?: File(System.getProperty("user.home")).absolutePath
                updateState { stateShowFileDialog().stateNavigateToDirectory(startDir) }
            }
            is MergerAction.OnDismissFileDialog -> {
                updateState { stateDismissFileDialog() }
            }
            is MergerAction.OnNavigateUp -> {
                updateState { stateNavigateUp() }
            }
            is MergerAction.OnNavigateToDirectory -> {
                updateState { stateNavigateToDirectory(action.path) }
                preferencesRepository.saveLastDirectory(File(action.path))
            }
            is MergerAction.OnNavigateToCustomPath -> {
                val dir = File(action.path)
                if (dir.exists() && dir.isDirectory) {
                    updateState { stateNavigateToCustomPath(action.path) }
                    preferencesRepository.saveLastDirectory(dir)
                }
            }
            is MergerAction.OnToggleFileSelection -> {
                updateState { stateToggleFileSelection(action.path) }
            }
            is MergerAction.OnConfirmFileDialog -> {
                if (state.selectedFiles.isNotEmpty()) {
                    val dir = File(state.dialogCurrentDirectory)
                    preferencesRepository.saveLastDirectory(dir)
                    updateState {
                        stateConfirmFileDialog()
                    }
                }
            }
            is MergerAction.OnUpdateDialogPathText -> {
                updateState { stateUpdateDialogPathText(action.path) }
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

            val progressTracker = MergeProgressTracker()

            val progressJob = viewModelScope.launch {
                progressTracker.progressFlow
                    .catch { updateState { stateMergeError("Ошибка отслеживания прогресса") } }
                    .collect { update ->
                        updateState { stateProgressUpdate(update.progress, update.operation) }
                    }
            }

            try {
                val outputPath = generateOutputPath(state.selectedFiles, state.outputFileName)

                val success = fileRepository.mergeFiles(
                    sourcePaths = state.selectedFiles,
                    targetPath = outputPath,
                    progressTracker = progressTracker
                )

                progressJob.cancel()

                if (success) {
                    updateState { stateMergeSuccess(outputPath) }
                } else {
                    updateState { stateMergeError("Ошибка при объединении файлов") }
                }
            } catch (e: Exception) {
                progressJob.cancel()
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
