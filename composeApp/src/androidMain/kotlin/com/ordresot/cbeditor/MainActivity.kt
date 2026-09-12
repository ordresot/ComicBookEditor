package com.ordresot.cbeditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ordresot.cbeditor.presentation.ComicEditorApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initKoin(application)

        setContent {
            MaterialTheme {
                ComicEditorApp(Platform.Android)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    ComicEditorApp(Platform.Android)
}
