package com.ordresot.cbeditor.presentation.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Merge
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ordresot.cbeditor.presentation.navigation.NavigationEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun NavigationBar(
    currentScreen: NavigationEffect,
    onScreenChange: (NavigationEffect) -> Unit
) {
    val isSelected: (NavigationEffect) -> Boolean = { it == currentScreen }

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularNavigationButton(
                onClick = { onScreenChange(NavigationEffect.NavigateToMainScreen) },
                isSelected = isSelected(NavigationEffect.NavigateToMainScreen),
                icon = Icons.Default.Home,
                contentDescription = "Главная"
            )

            CircularNavigationButton(
                onClick = { onScreenChange(NavigationEffect.NavigateToEditorScreen) },
                isSelected = isSelected(NavigationEffect.NavigateToEditorScreen),
                icon = Icons.Default.Edit,
                contentDescription = "Редактировать"
            )

            CircularNavigationButton(
                onClick = { onScreenChange(NavigationEffect.NavigateToMergerScreen) },
                isSelected = isSelected(NavigationEffect.NavigateToMergerScreen),
                icon = Icons.Default.Merge,
                contentDescription = "Объединить"
            )

            CircularNavigationButton(
                onClick = { onScreenChange(NavigationEffect.NavigateToConverterScreen) },
                isSelected = isSelected(NavigationEffect.NavigateToConverterScreen),
                icon = Icons.Default.SwapHoriz,
                contentDescription = "Конвертировать"
            )
        }
    }
}
