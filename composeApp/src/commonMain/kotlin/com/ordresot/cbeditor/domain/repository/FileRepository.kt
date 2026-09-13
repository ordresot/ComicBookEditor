package com.ordresot.cbeditor.domain.repository

import com.ordresot.cbeditor.presentation.utils.MergeProgressTracker

interface FileRepository {
    suspend fun readFile(path: String): ByteArray?
    suspend fun fileExists(path: String): Boolean
    suspend fun mergeFiles(
        sourcePaths: List<String>,
        targetPath: String,
        progressTracker: MergeProgressTracker? = null
    ): Boolean
    suspend fun saveFile(data: ByteArray, targetPath: String): Boolean
}