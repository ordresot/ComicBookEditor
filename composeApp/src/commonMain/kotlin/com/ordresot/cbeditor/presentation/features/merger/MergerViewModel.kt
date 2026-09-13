package com.ordresot.cbeditor.presentation.features.merger

import androidx.lifecycle.viewModelScope
import com.ordresot.cbeditor.presentation.navigation.NavigationEffect
import com.ordresot.cbeditor.presentation.core.base.BaseViewModel
import com.ordresot.cbeditor.domain.repository.FileRepository
import com.ordresot.cbeditor.domain.repository.PreferencesRepository
import com.ordresot.cbeditor.presentation.utils.MergeProgressTracker
import com.ordresot.cbeditor.core.utils.generateOutputPath
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
            is MergerAction.OnFilesSelected -> onFilesSelected(action.files)
            is MergerAction.OnOutputFileNameChanged -> onOutputFileNameChanged(action.fileName)
            is MergerAction.OnMergeFiles -> onMergeFiles()
            is MergerAction.OnDismissError -> onDismissError()
            is MergerAction.OnDismissSuccess -> onDismissSuccess()
            is MergerAction.OnShowFileDialog -> onShowFileDialog()
            is MergerAction.OnDismissFileDialog -> onDismissFileDialog()
            is MergerAction.OnNavigateUp -> onNavigateUp()
            is MergerAction.OnNavigateToDirectory -> onNavigateToDirectory(action.path)
            is MergerAction.OnNavigateToCustomPath -> onNavigateToCustomPath(action.path)
            is MergerAction.OnToggleFileSelection -> onToggleFileSelection(action.path)
            is MergerAction.OnConfirmFileDialog -> onConfirmFileDialog()
            is MergerAction.OnUpdateDialogPathText -> onUpdateDialogPathText(action.path)
            is MergerAction.OnProgressUpdate -> onProgressUpdate(action.progress, action.operation)
        }
    }

    private fun onFilesSelected(files: List<String>) {
        updateState { stateSelectedFiles(files) }
        if (files.isNotEmpty()) {
            val firstFileName = File(files.first()).nameWithoutExtension
            updateState { stateOutputFileName(firstFileName) }
        } else {
            updateState { stateOutputFileName("") }
        }
    }

    private fun onOutputFileNameChanged(fileName: String) {
        updateState { stateOutputFileName(fileName) }
    }

    private fun onDismissError() {
        updateState { stateDismissMessage() }
    }

    private fun onDismissSuccess() {
        updateState { stateDismissMessage() }
    }

    private fun onShowFileDialog() {
        viewModelScope.launch {
            val lastDir = preferencesRepository.getLastDirectory()
            val startDir = lastDir?.absolutePath ?: File(System.getProperty("user.home")).absolutePath
            updateState { stateShowFileDialog().stateNavigateToDirectory(startDir) }
        }
    }

    private fun onDismissFileDialog() {
        updateState { stateDismissFileDialog() }
    }

    private fun onNavigateUp() {
        updateState { stateNavigateUp() }
    }

    private fun onNavigateToDirectory(path: String) {
        updateState { stateNavigateToDirectory(path) }
        viewModelScope.launch {
            preferencesRepository.saveLastDirectory(File(path))
        }
    }

    private fun onNavigateToCustomPath(path: String) {
        val dir = File(path)
        if (dir.exists() && dir.isDirectory) {
            updateState { stateNavigateToCustomPath(path) }
            viewModelScope.launch {
                preferencesRepository.saveLastDirectory(dir)
            }
        }
    }

    private fun onToggleFileSelection(path: String) {
        updateState { stateToggleFileSelection(path) }
    }

    private fun onConfirmFileDialog() {
        if (state.selectedFiles.isNotEmpty()) {
            val dir = File(state.dialogCurrentDirectory)
            viewModelScope.launch {
                preferencesRepository.saveLastDirectory(dir)
            }
            updateState { stateConfirmFileDialog() }
        }
    }

    private fun onUpdateDialogPathText(path: String) {
        updateState { stateUpdateDialogPathText(path) }
    }

    private fun onProgressUpdate(progress: Float, operation: String) {
        updateState { stateProgressUpdate(progress, operation) }
    }

    private fun onMergeFiles() {
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
}
