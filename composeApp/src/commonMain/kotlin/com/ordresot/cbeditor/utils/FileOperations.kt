package com.ordresot.cbeditor.utils

expect class FileOperations() {
    suspend fun readFile(path: String): ByteArray?
    suspend fun fileExists(path: String): Boolean
}
