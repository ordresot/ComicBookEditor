package com.ordresot.cbeditor.domain.repository

import java.io.File

interface PreferencesRepository {
    fun saveLastDirectory(directory: File)
    fun getLastDirectory(): File?
    fun getDefaultDirectory(): File
}