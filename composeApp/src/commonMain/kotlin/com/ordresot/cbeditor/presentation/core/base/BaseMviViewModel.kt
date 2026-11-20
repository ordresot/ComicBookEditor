package com.ordresot.cbeditor.presentation.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Базовая ViewModel для MVI-архитектуры.
 * Централизует хранение State и Effect, предоставляет единый вход для Intent.
 *
 * @param INTENT тип Intent (входные события)
 * @param STATE тип State (отображаемое состояние)
 * @param EFFECT тип Effect (одноразовые события)
 */
abstract class BaseMviViewModel<
    INTENT : MviIntent,
    STATE : MviState,
    EFFECT : MviEffect
>(
    initialState: STATE
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state

    private val _effect = MutableSharedFlow<EFFECT>(extraBufferCapacity = 1)
    val effect: SharedFlow<EFFECT> = _effect

    /**
     * Публичная точка приёма Intent от UI.
     */
    fun onIntent(intent: INTENT) {
        handleIntent(intent)
    }

    /**
     * Абстрактный метод для обработки конкретного Intent в наследнике.
     */
    protected abstract fun handleIntent(intent: INTENT)

    /**
     * Создание и публикация нового состояния через редуктор.
     */
    protected fun setState(reducer: STATE.() -> STATE) {
        _state.update { currentState().reducer() }
    }

    /**
     * Чтение актуального состояния.
     */
    protected fun currentState(): STATE = _state.value

    /**
     * Отправка одноразового события.
     */
    protected fun sendEffect(effect: EFFECT) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }
}
