package com.ordresot.cbeditor.data.repository

import com.ordresot.cbeditor.domain.repository.FileRepository
import com.ordresot.cbeditor.presentation.utils.MergeProgressTracker

expect class FileRepositoryImpl() : FileRepository {
    override suspend fun readFile(path: String): ByteArray?
    override suspend fun fileExists(path: String): Boolean
    override suspend fun mergeFiles(
        sourcePaths: List<String>,
        targetPath: String,
        progressTracker: MergeProgressTracker?
    ): Boolean
    override suspend fun saveFile(data: ByteArray, targetPath: String): Boolean
}