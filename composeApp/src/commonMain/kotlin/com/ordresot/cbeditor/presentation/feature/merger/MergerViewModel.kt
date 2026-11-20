package com.ordresot.cbeditor.presentation.feature.merger

import androidx.lifecycle.viewModelScope
import com.ordresot.cbeditor.core.utils.generateOutputPath
import com.ordresot.cbeditor.domain.repository.FileRepository
import com.ordresot.cbeditor.domain.repository.PreferencesRepository
import com.ordresot.cbeditor.presentation.core.base.BaseMviViewModel
import com.ordresot.cbeditor.presentation.utils.MergeProgressTracker
import java.io.File
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * MVI ViewModel для экрана объединения файлов.
 * Содержит бизнес-логику экрана, не зависит от Voyager.
 */
class MergerViewModel(
    private val fileRepository: FileRepository,
    private val preferencesRepository: PreferencesRepository
) : BaseMviViewModel<MergerIntent, MergerState, MergerEffect>(
    initialState = MergerState()
) {

    override fun handleIntent(intent: MergerIntent) {
        when (intent) {
            is MergerIntent.OnFilesSelected -> onFilesSelected(intent.files)
            is MergerIntent.OnOutputFileNameChanged -> onOutputFileNameChanged(intent.fileName)
            is MergerIntent.OnMergeFiles -> onMergeFiles()
            is MergerIntent.OnDismissError -> onDismissError()
            is MergerIntent.OnDismissSuccess -> onDismissSuccess()
            is MergerIntent.OnShowFileDialog -> onShowFileDialog()
            is MergerIntent.OnDismissFileDialog -> onDismissFileDialog()
            is MergerIntent.OnNavigateUp -> onNavigateUp()
            is MergerIntent.OnNavigateToDirectory -> onNavigateToDirectory(intent.path)
            is MergerIntent.OnNavigateToCustomPath -> onNavigateToCustomPath(intent.path)
            is MergerIntent.OnToggleFileSelection -> onToggleFileSelection(intent.path)
            is MergerIntent.OnSelectAll -> onSelectAll()
            is MergerIntent.OnClearSelection -> onClearSelection()
            is MergerIntent.OnUpdateDialogPathText -> onUpdateDialogPathText(intent.path)
            is MergerIntent.OnProgressUpdate -> onProgressUpdate(intent.progress, intent.operation)
        }
    }

    private fun onFilesSelected(files: List<String>) {
        setState { stateSelectedFiles(files) }
        if (files.isNotEmpty()) {
            val firstFileName = File(files.first()).nameWithoutExtension
            setState { stateOutputFileName(firstFileName) }
        } else {
            setState { stateOutputFileName("") }
        }
    }

    private fun onOutputFileNameChanged(fileName: String) {
        setState { stateOutputFileName(fileName) }
    }

    private fun onDismissError() {
        setState { stateDismissMessage() }
    }

    private fun onDismissSuccess() {
        setState { stateDismissMessage() }
    }

    private fun onShowFileDialog() {
        viewModelScope.launch {
            val lastDir = preferencesRepository.getLastDirectory()
            val startDir = lastDir?.absolutePath ?: File(System.getProperty("user.home")).absolutePath
            setState { stateShowFileDialog().stateNavigateToDirectory(startDir) }
        }
    }

    private fun onDismissFileDialog() {
        setState { stateDismissFileDialog() }
    }

    private fun onNavigateUp() {
        setState { stateNavigateUp() }
    }

    private fun onNavigateToDirectory(path: String) {
        setState { stateNavigateToDirectory(path) }
        viewModelScope.launch {
            preferencesRepository.saveLastDirectory(File(path))
        }
    }

    private fun onNavigateToCustomPath(path: String) {
        val dir = File(path)
        if (dir.exists() && dir.isDirectory) {
            setState { stateNavigateToCustomPath(path) }
            viewModelScope.launch {
                preferencesRepository.saveLastDirectory(dir)
            }
        }
    }

    private fun onToggleFileSelection(path: String) {
        setState { stateToggleFileSelection(path) }
    }

    private fun onSelectAll() {
        val currentDir = File(currentState().dialogCurrentDirectory)
        val comicFiles = currentDir.listFiles()?.filter { isComicFile(it) }?.map { it.absolutePath }
            ?: emptyList()
        if (comicFiles.isNotEmpty()) {
            setState { stateSelectedFiles(comicFiles) }
        }
    }

    private fun onClearSelection() {
        setState { stateClearSelection() }
    }

    private fun onUpdateDialogPathText(path: String) {
        setState { stateUpdateDialogPathText(path) }
    }

    private fun onProgressUpdate(progress: Float, operation: String) {
        setState { stateProgressUpdate(progress, operation) }
    }

    private fun onMergeFiles() {
        val state = currentState()
        if (state.selectedFiles.size < 2) return

        viewModelScope.launch {
            setState { stateMergingStarted() }

            val progressTracker = MergeProgressTracker()

            val progressJob = viewModelScope.launch {
                progressTracker.progressFlow
                    .catch {
                        setState {
                            stateMergeError("Ошибка отслеживания прогресса")
                        }
                    }
                    .collect { update ->
                        setState { stateProgressUpdate(update.progress, update.operation) }
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
                    setState { stateMergeSuccess(outputPath) }
                } else {
                    setState { stateMergeError("Ошибка при объединении файлов") }
                }
            } catch (e: Exception) {
                progressJob.cancel()
                setState { stateMergeError("Ошибка: ${e.message}") }
            }
        }
    }

    private fun isComicFile(file: File): Boolean {
        return file.name.lowercase().let {
            it.endsWith(".cbr") || it.endsWith(".cbz")
        }
    }
}
