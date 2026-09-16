package com.ordresot.cbeditor.presentation.core.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.ordresot.cbeditor.presentation.navigation.NavigationEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
expect fun NavigationBar(
    currentScreen: NavigationEffect,
    onScreenChange: (NavigationEffect) -> Unit
)
