package com.ordresot.cbeditor.core.di

import com.ordresot.cbeditor.data.repository.ArchiveRepositoryImpl
import com.ordresot.cbeditor.data.repository.FileRepositoryImpl
import com.ordresot.cbeditor.data.repository.PreferencesRepositoryImpl
import com.ordresot.cbeditor.domain.repository.ArchiveRepository
import com.ordresot.cbeditor.domain.repository.FileRepository
import com.ordresot.cbeditor.domain.repository.PreferencesRepository
import com.ordresot.cbeditor.presentation.features.merger.MergerViewModel
import org.koin.dsl.module

val repositoryModule = module {
    single<ArchiveRepository> { ArchiveRepositoryImpl() }
    single<FileRepository> { FileRepositoryImpl() }
    single<PreferencesRepository> { PreferencesRepositoryImpl() }
}

val viewModelModule = module {
    factory { MergerViewModel() }
}

val appModule = listOf(
    repositoryModule,
    viewModelModule
)
