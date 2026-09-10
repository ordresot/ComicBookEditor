package com.ordresot.cbeditor.data.repository

import com.ordresot.cbeditor.domain.repository.ArchiveRepository
import com.ordresot.cbeditor.models.ArchiveEntry
import com.ordresot.cbeditor.utils.MergeProgressTracker

expect class ArchiveRepositoryImpl() : ArchiveRepository {
    override suspend fun extractFiles(
        archivePath: String,
        progressTracker: MergeProgressTracker?
    ): List<ArchiveEntry>

    override suspend fun createArchive(
        entries: List<ArchiveEntry>,
        outputPath: String,
        progressTracker: MergeProgressTracker?
    ): Boolean
}