package com.ordresot.cbeditor

import com.ordresot.cbeditor.core.di.appModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
