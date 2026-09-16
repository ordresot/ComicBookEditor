package com.ordresot.cbeditor.presentation.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LargeNavigationButton(
    onClick: () -> Unit,
    isSelected: Boolean,
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(containerColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = rememberVectorPainter(icon),
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
                color = contentColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CircularNavigationButton(
    onClick: () -> Unit,
    isSelected: Boolean,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(containerColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = rememberVectorPainter(icon),
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(28.dp)
        )
    }
}