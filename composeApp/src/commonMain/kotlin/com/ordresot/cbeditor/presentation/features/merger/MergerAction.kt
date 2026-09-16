package com.ordresot.cbeditor.presentation.features.merger

sealed interface MergerAction {
    data object OnMergeFiles : MergerAction
    data object OnDismissError : MergerAction
    data object OnDismissSuccess : MergerAction
    data object OnShowFileDialog : MergerAction
    data object OnDismissFileDialog : MergerAction
    data object OnNavigateUp : MergerAction
    data class OnNavigateToDirectory(val path: String) : MergerAction
    data class OnNavigateToCustomPath(val path: String) : MergerAction
    data class OnToggleFileSelection(val path: String) : MergerAction
    data object OnSelectAll : MergerAction
    data object OnConfirmFileDialog : MergerAction
    data class OnUpdateDialogPathText(val path: String) : MergerAction
    data class OnFilesSelected(val files: List<String>) : MergerAction
    data class OnOutputFileNameChanged(val fileName: String) : MergerAction
    data class OnProgressUpdate(val progress: Float, val operation: String) : MergerAction
}
