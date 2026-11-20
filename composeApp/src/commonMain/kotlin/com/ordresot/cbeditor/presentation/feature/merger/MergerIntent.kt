package com.ordresot.cbeditor.presentation.feature.merger

import com.ordresot.cbeditor.presentation.core.base.MviIntent

/**
 * MVI Intents для экрана объединения файлов.
 * Описывает действия пользователя, которые поступают в ViewModel.
 */
sealed interface MergerIntent : MviIntent {
    data object OnMergeFiles : MergerIntent
    data object OnDismissError : MergerIntent
    data object OnDismissSuccess : MergerIntent
    data object OnShowFileDialog : MergerIntent
    data object OnDismissFileDialog : MergerIntent
    data object OnNavigateUp : MergerIntent
    data class OnNavigateToDirectory(val path: String) : MergerIntent
    data class OnNavigateToCustomPath(val path: String) : MergerIntent
    data class OnToggleFileSelection(val path: String) : MergerIntent
    data object OnSelectAll : MergerIntent
    data object OnClearSelection : MergerIntent
    data class OnUpdateDialogPathText(val path: String) : MergerIntent
    data class OnFilesSelected(val files: List<String>) : MergerIntent
    data class OnOutputFileNameChanged(val fileName: String) : MergerIntent
    data class OnProgressUpdate(val progress: Float, val operation: String) : MergerIntent
}
