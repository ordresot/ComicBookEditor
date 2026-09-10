package com.ordresot.cbeditor.data.repository

actual class FileRepositoryImpl actual constructor() {
    actual suspend fun pickFiles(): List<String>? {
        TODO("Not yet implemented")
    }

    actual suspend fun readFile(path: String): ByteArray? {
        TODO("Not yet implemented")
    }

    actual suspend fun fileExists(path: String): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun mergeFiles(
        sourcePaths: List<String>,
        targetPath: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun saveFile(data: ByteArray, targetPath: String): Boolean {
        TODO("Not yet implemented")
    }
}