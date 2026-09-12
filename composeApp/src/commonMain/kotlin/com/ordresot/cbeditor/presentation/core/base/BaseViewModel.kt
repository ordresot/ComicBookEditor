package com.ordresot.cbeditor.presentation.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE, UI_STATE, ACTION, EFFECT>(
    private val converter: StateConverter<STATE, UI_STATE>,
    private val initState: STATE
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(initState)
    private val stateFlow: StateFlow<STATE> = _stateFlow
    protected val state: STATE get() = stateFlow.value
    protected fun updateState(update: STATE.() -> STATE) {
        _stateFlow.update(update)
    }

    private fun StateFlow<STATE>.uiConverter(): StateFlow<UI_STATE> {
        return map(converter::uiMap).stateIn(
            scope = viewModelScope,
            started = Lazily,
            initialValue = converter.uiMap(initState)
        )
    }

    private val _uiEffects: MutableSharedFlow<EFFECT> = MutableSharedFlow(replay = 0)

    fun sendEffect(effect: EFFECT) {
        viewModelScope.launch {
            _uiEffects.emit(effect)
        }
    }

    abstract fun onAction(action: ACTION)

    val uiEffects: SharedFlow<EFFECT> get() = _uiEffects.asSharedFlow()

    val uiState: StateFlow<UI_STATE> = stateFlow.uiConverter()
}