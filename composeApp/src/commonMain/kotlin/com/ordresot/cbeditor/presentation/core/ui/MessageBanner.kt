package com.ordresot.cbeditor.presentation.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.ordresot.cbeditor.presentation.features.merger.uiState.MessageUi

@Composable
fun MessageBanner(
    message: MessageUi?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (message != null) {
        val (icon, containerColor, contentColor) = when (message.type) {
            MessageUi.MessageType.Success -> Triple(Icons.Default.CheckCircle,
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.onPrimaryContainer)
            MessageUi.MessageType.Error -> Triple(Icons.Default.Error,
                MaterialTheme.colorScheme.errorContainer,
                MaterialTheme.colorScheme.onErrorContainer)
        }

        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = rememberVectorPainter(icon),
                    contentDescription = message.type.name,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor,
                    modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        painter = rememberVectorPainter(Icons.Default.Close),
                        contentDescription = "Закрыть",
                        tint = contentColor
                    )
                }
            }
        }
    }
}

enum class MessageType { Success, Error }
