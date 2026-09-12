package com.ordresot.cbeditor

import android.app.Application
import com.ordresot.cbeditor.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

fun initKoin(application: Application) {
    startKoin {
        androidLogger(Level.ERROR)
        androidContext(application)
        modules(appModule)
    }
}
