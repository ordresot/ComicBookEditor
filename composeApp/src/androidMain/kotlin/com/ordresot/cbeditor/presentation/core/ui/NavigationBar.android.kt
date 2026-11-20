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
import com.ordresot.cbeditor.presentation.navigation.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun NavigationBar(
    currentRoute: ScreenRoute,
    onRouteChange: (ScreenRoute) -> Unit
) {
    val isSelected: (ScreenRoute) -> Boolean = { it == currentRoute }

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularNavigationButton(
                onClick = { onRouteChange(ScreenRoute.Main) },
                isSelected = isSelected(ScreenRoute.Main),
                icon = Icons.Default.Home,
                contentDescription = "Главная"
            )

            CircularNavigationButton(
                onClick = { onRouteChange(ScreenRoute.Editor) },
                isSelected = isSelected(ScreenRoute.Editor),
                icon = Icons.Default.Edit,
                contentDescription = "Редактировать"
            )

            CircularNavigationButton(
                onClick = { onRouteChange(ScreenRoute.Merger) },
                isSelected = isSelected(ScreenRoute.Merger),
                icon = Icons.Default.Merge,
                contentDescription = "Объединить"
            )

            CircularNavigationButton(
                onClick = { onRouteChange(ScreenRoute.Converter) },
                isSelected = isSelected(ScreenRoute.Converter),
                icon = Icons.Default.SwapHoriz,
                contentDescription = "Конвертировать"
            )
        }
    }
}
