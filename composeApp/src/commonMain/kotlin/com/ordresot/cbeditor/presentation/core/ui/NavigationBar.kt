package com.ordresot.cbeditor.presentation.core.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.ordresot.cbeditor.presentation.navigation.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
expect fun NavigationBar(
    currentRoute: ScreenRoute,
    onRouteChange: (ScreenRoute) -> Unit
)
