package com.ordresot.cbeditor.presentation.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.ordresot.cbeditor.presentation.core.base.MviState

/**
 * Состояние главного экрана (stub).
 */
data class MainState(
    val message: String = "Comic Book Editor"
) : MviState

/**
 * Voyager Screen для главного экрана.
 */
class MainScreen : Screen {
    @Composable
    override fun Content() {
        MainContent()
    }
}

/**
 * Pure Compose UI главного экрана.
 */
@Composable
fun MainContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Comic Book Editor",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 48.dp)
        )
    }
}
