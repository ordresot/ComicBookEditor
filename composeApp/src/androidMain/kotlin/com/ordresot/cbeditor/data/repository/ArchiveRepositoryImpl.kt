package com.ordresot.cbeditor.data.repository

actual class ArchiveRepositoryImpl actual constructor() {
    actual suspend fun extractFiles(archivePath: String): List<ArchiveEntry> {
        TODO("Not yet implemented")
    }

    actual suspend fun createArchive(
        entries: List<ArchiveEntry>,
        outputPath: String
    ): Boolean {
        TODO("Not yet implemented")
    }
}