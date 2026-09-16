package com.ordresot.cbeditor.presentation.core.ui

import androidx.compose.runtime.Composable

@Composable
expect fun FileDialog(
    isShowDialog: Boolean,
    currentDirectory: String,
    selectedFiles: List<String>,
    pathText: String,
    onNavigateUp: () -> Unit,
    onNavigateToDirectory: (String) -> Unit,
    onNavigateToCustomPath: (String) -> Unit,
    onToggleFileSelection: (String) -> Unit,
    onSelectAll: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onUpdatePathText: (String) -> Unit
)
