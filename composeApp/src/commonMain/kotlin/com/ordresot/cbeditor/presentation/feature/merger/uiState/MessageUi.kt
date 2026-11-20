package com.ordresot.cbeditor.presentation.feature.merger.uiState

data class MessageUi(
    val text: String,
    val type: MessageType
) {
    enum class MessageType { Error, Success }
}