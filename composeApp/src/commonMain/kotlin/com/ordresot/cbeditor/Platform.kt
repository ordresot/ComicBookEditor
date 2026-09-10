package com.ordresot.cbeditor

sealed class Platform {
    object Android: Platform()
    object Desktop: Platform()
}

expect fun getPlatform(): Platform