package com.ordresot.cbeditor.presentation.features.merger.uiState

data class MessageUi(
    val text: String,
    val type: MessageType
) {
    enum class MessageType { Error, Success }
}