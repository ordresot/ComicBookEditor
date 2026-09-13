package com.ordresot.cbeditor.domain.repository

import com.ordresot.cbeditor.models.ArchiveEntry
import com.ordresot.cbeditor.presentation.utils.MergeProgressTracker

interface ArchiveRepository {
    suspend fun extractFiles(archivePath: String, progressTracker: MergeProgressTracker? = null) : List<ArchiveEntry>
    suspend fun createArchive(entries: List<ArchiveEntry>, outputPath: String, progressTracker: MergeProgressTracker? = null) : Boolean
}