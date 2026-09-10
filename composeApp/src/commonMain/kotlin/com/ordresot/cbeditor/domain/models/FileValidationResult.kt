package com.ordresot.cbeditor.models

data class FileValidationResult(
    val path: String,
    val exists: Boolean,
    val isValidFormat: Boolean,
    val errorMessage: String?
)