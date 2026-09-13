package com.ordresot.cbeditor.core.utils

import java.io.File

fun generateOutputPath(sourcePaths: List<String>, customFileName: String): String {
    val firstFile = File(sourcePaths.first())
    val parentDir = firstFile.parent ?: System.getProperty("user.home")
    val fileName = customFileName.ifEmpty { firstFile.nameWithoutExtension }
    return "$parentDir/${fileName}.cbz"
}