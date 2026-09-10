package com.ordresot.cbeditor.utils

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf

class MergeProgressTracker {
    private var _currentProgress = mutableFloatStateOf(0f)
    private var _currentOperation = mutableStateOf("")

    val currentProgress: Float get() = _currentProgress.value
    val currentOperation: String get() = _currentOperation.value

    fun updateProgress(progress: Float, operation: String = "") {
        _currentProgress.floatValue = progress
        if (operation.isNotEmpty()) {
            _currentOperation.value = operation
        }
    }

    fun reset() {
        _currentProgress.floatValue = 0f
        _currentOperation.value = ""
    }
}