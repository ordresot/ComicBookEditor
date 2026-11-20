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