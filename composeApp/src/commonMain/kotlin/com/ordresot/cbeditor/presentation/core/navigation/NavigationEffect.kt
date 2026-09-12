package com.ordresot.cbeditor.presentation.core.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationEffect {
    @Serializable
    data object NavigateToMainScreen : NavigationEffect

    @Serializable
    data object NavigateToConverterScreen : NavigationEffect

    @Serializable
    data object NavigateToMergerScreen : NavigationEffect

    @Serializable
    data object NavigateToEditorScreen : NavigationEffect
}