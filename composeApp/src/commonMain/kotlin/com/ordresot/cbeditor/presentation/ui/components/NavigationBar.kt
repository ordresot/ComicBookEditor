package com.ordresot.cbeditor.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Merge
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ordresot.cbeditor.Platform
import com.ordresot.cbeditor.getPlatform

@OptIn(ExperimentalMaterial3Api::class)
@Composable
expect fun NavigationBar(navController: NavHostController)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DesktopNavigationBar(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    if (currentDestination in BottomNavItem.items.map { it.route }) {
        CenterAlignedTopAppBar(
            modifier = Modifier.padding(vertical = 16.dp),
            title = {
                Text(
                    "Comic Editor",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            actions = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    LargeNavigationButton(
                        onClick = { onScreenChange(Screen.Main) },
                        isSelected = currentScreen == Screen.Main,
                        icon = Icons.Default.Home,
                        text = "Главная",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    LargeNavigationButton(
                        onClick = { onScreenChange(Screen.EditFile) },
                        isSelected = currentScreen == Screen.EditFile,
                        icon = Icons.Default.Edit,
                        text = "Редактировать",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    LargeNavigationButton(
                        onClick = { onScreenChange(Screen.MergeFiles) },
                        isSelected = currentScreen == Screen.MergeFiles,
                        icon = Icons.Default.Merge,
                        text = "Объединить",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    LargeNavigationButton(
                        onClick = { onScreenChange(Screen.ConvertFile) },
                        isSelected = currentScreen == Screen.ConvertFile,
                        icon = Icons.Default.SwapHoriz,
                        text = "Конвертировать",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Composable
private fun AndroidNavigationBar(
    navController: NavHostController,
    onScreenChange: (Screen) -> Unit
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularNavigationButton(
                onClick = { onScreenChange(Screen.Main) },
                isSelected = currentScreen == Screen.Main,
                icon = Icons.Default.Home,
                contentDescription = "Главная"
            )

            CircularNavigationButton(
                onClick = { onScreenChange(Screen.EditFile) },
                isSelected = currentScreen == Screen.EditFile,
                icon = Icons.Default.Edit,
                contentDescription = "Редактировать"
            )

            CircularNavigationButton(
                onClick = { onScreenChange(Screen.MergeFiles) },
                isSelected = currentScreen == Screen.MergeFiles,
                icon = Icons.Default.Merge,
                contentDescription = "Объединить"
            )

            CircularNavigationButton(
                onClick = { onScreenChange(Screen.ConvertFile) },
                isSelected = currentScreen == Screen.ConvertFile,
                icon = Icons.Default.SwapHoriz,
                contentDescription = "Конвертировать"
            )
        }
    }
}