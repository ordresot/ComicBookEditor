package com.ordresot.cbeditor.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Маршруты навигации приложения.
 * Каждый маршрут соответствует одному экрану Voyager.
 */
@Serializable
sealed interface ScreenRoute {
    @Serializable
    data object Main : ScreenRoute

    @Serializable
    data object Editor : ScreenRoute

    @Serializable
    data object Merger : ScreenRoute

    @Serializable
    data object Converter : ScreenRoute
}

/**
 * Ключ таба для ScreenRoute.
 */
val ScreenRoute.tabKey: String
    get() = when (this) {
        ScreenRoute.Main -> "main"
        ScreenRoute.Editor -> "editor"
        ScreenRoute.Merger -> "merger"
        ScreenRoute.Converter -> "converter"
    }
