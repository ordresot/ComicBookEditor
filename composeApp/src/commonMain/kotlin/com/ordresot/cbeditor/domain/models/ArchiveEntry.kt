package com.ordresot.cbeditor.models

data class ArchiveEntry(
    val name: String,
    val data: ByteArray,
    val originalArchive: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ArchiveEntry
        if (name != other.name) return false
        if (!data.contentEquals(other.data)) return false
        if (originalArchive != other.originalArchive) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + originalArchive.hashCode()
        return result
    }
}