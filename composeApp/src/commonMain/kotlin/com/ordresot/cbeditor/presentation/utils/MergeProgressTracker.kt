package com.ordresot.cbeditor.presentation.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class ProgressUpdate(
    val progress: Float,
    val operation: String
)

class MergeProgressTracker {
    private val _progressFlow = MutableSharedFlow<ProgressUpdate>(extraBufferCapacity = 1)
    val progressFlow: SharedFlow<ProgressUpdate> = _progressFlow.asSharedFlow()

    suspend fun updateProgress(progress: Float, operation: String = "") {
        _progressFlow.emit(ProgressUpdate(progress, operation))
    }

    fun reset() {
        _progressFlow.tryEmit(ProgressUpdate(0f, ""))
    }
}
