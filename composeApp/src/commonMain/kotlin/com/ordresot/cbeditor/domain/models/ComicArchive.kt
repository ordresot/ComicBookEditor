package com.ordresot.cbeditor.models

import com.ordresot.cbeditor.domain.models.ComicFormat

data class ComicArchive(
    val id: String,
    val name: String,
    val format: ComicFormat,
    val pages: List<ComicPage>,
    val filePath: String
)