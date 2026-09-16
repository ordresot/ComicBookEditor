package com.ordresot.cbeditor.presentation.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Merge
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ordresot.cbeditor.presentation.navigation.NavigationEffect

@Composable
actual fun NavigationBar(
    currentScreen: NavigationEffect,
    onScreenChange: (NavigationEffect) -> Unit
) {
    val isSelected: (NavigationEffect) -> Boolean = { it == currentScreen }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 16.dp,
                horizontal = 40.dp

            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            LargeNavigationButton(
                onClick = { onScreenChange(NavigationEffect.NavigateToMainScreen) },
                isSelected = isSelected(NavigationEffect.NavigateToMainScreen),
                icon = Icons.Default.Home,
                text = "Главная",
                modifier = Modifier.weight(1f)
            )

            LargeNavigationButton(
                onClick = { onScreenChange(NavigationEffect.NavigateToEditorScreen) },
                isSelected = isSelected(NavigationEffect.NavigateToEditorScreen),
                icon = Icons.Default.Edit,
                text = "Редактировать",
                modifier = Modifier.weight(1f)
            )

            LargeNavigationButton(
                onClick = { onScreenChange(NavigationEffect.NavigateToMergerScreen) },
                isSelected = isSelected(NavigationEffect.NavigateToMergerScreen),
                icon = Icons.Default.Merge,
                text = "Объединить",
                modifier = Modifier.weight(1f)
            )

            LargeNavigationButton(
                onClick = { onScreenChange(NavigationEffect.NavigateToConverterScreen) },
                isSelected = isSelected(NavigationEffect.NavigateToConverterScreen),
                icon = Icons.Default.SwapHoriz,
                text = "Конвертировать",
                modifier = Modifier.weight(1f)
            )
        }
    }
}
