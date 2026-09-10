package com.ordresot.cbeditor.data.repository

import com.ordresot.cbeditor.core.Constants
import com.ordresot.cbeditor.domain.repository.PreferencesRepository
import java.io.File
import java.util.prefs.Preferences

class PreferencesRepositoryImpl : PreferencesRepository {
    private val preferences: Preferences = Preferences.userNodeForPackage(PreferencesRepositoryImpl::class.java)

    override fun saveLastDirectory(directory: File) {
        try {
            if (directory.exists() && directory.isDirectory) {
                preferences.put(Constants.LAST_DIRECTORY_KEY, directory.absolutePath)
                preferences.flush()
            }
        } catch (e: Exception) {
            println("Ошибка при сохранении директории: ${e.message}")
        }
    }

    override fun getLastDirectory(): File? {
        return try {
            val path = preferences.get(Constants.LAST_DIRECTORY_KEY, null)
            path?.let { File(it) }?.takeIf { it.exists() && it.isDirectory }
        } catch (e: Exception) {
            null
        }
    }

    override fun getDefaultDirectory(): File {
        return getLastDirectory() ?: File(System.getProperty("user.home"))
    }
}