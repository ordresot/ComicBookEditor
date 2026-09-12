package com.ordresot.cbeditor.presentation.merger

sealed interface MergerAction {
    data object OnMergeFiles : MergerAction
    data object OnDismissError : MergerAction
    data object OnDismissSuccess : MergerAction
    data object OnShowFileDialog : MergerAction
    data object OnDismissFileDialog : MergerAction
    data class OnFilesSelected(val files: List<String>) : MergerAction
    data class OnOutputFileNameChanged(val fileName: String) : MergerAction
    data class OnProgressUpdate(val progress: Float, val operation: String) : MergerAction
}
