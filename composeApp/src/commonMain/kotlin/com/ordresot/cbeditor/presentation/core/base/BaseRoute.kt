package com.ordresot.cbeditor.presentation.core.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription

/**
 * Универсальный Compose-компонент, связывающий MVI ViewModel с UI.
 *
 * Обязанности:
 * - подписывается на State с учётом жизненного цикла;
 * - собирает поток Effect;
 * - передаёт State в UI;
 * - передаёт callback для отправки Intent;
 * - делегирует обработку Effect вызывающему экрану.
 *
 * @param INTENT тип Intent
 * @param STATE тип State
 * @param EFFECT тип Effect
 * @param viewModel MVI ViewModel экрана
 * @param onEffect обработчик одноразовых эффектов (навигация, сообщения)
 * @param content содержимое UI, получающее State и onIntent callback
 */
@Composable
fun <
    INTENT : MviIntent,
    STATE : MviState,
    EFFECT : MviEffect
> BaseRoute(
    viewModel: BaseMviViewModel<INTENT, STATE, EFFECT>,
    onEffect: suspend (EFFECT) -> Unit,
    content: @Composable (state: STATE, onIntent: (INTENT) -> Unit) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect
            .onSubscription { /* Effect collected */ }
            .onCompletion { /* Collection finished */ }
            .collect { effect ->
                onEffect(effect)
            }
    }

    content(state.value) { intent ->
        viewModel.onIntent(intent)
    }
}
