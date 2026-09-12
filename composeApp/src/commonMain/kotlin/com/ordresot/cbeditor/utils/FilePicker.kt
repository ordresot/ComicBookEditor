package com.ordresot.cbeditor.utils

expect class FilePicker() {
    suspend fun pickFiles(): List<String>?
}
