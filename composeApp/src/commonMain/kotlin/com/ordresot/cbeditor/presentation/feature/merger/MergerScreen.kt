package com.ordresot.cbeditor.presentation.feature.merger

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.ordresot.cbeditor.presentation.core.base.BaseRoute
import org.koin.compose.koinInject

/**
 * Voyager Screen для экрана объединения файлов.
 * Точка интеграции с Voyager: связывает ViewModel, BaseRoute и UI.
 */
class MergerScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: MergerViewModel = koinInject()

        BaseRoute(
            viewModel = viewModel,
            onEffect = { }
        ) { state, onIntent ->
            MergerContent(
                state = state,
                onIntent = onIntent
            )
        }
    }
}
