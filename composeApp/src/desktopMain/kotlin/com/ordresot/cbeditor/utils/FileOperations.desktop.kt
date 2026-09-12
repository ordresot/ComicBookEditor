package com.ordresot.cbeditor.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class FileOperations actual constructor() {
    actual suspend fun readFile(path: String): ByteArray? = withContext(Dispatchers.IO) {
        try {
            File(path).readBytes()
        } catch (e: Exception) {
            null
        }
    }

    actual suspend fun fileExists(path: String): Boolean = withContext(Dispatchers.IO) {
        File(path).exists()
    }
}
