package com.ordresot.cbeditor.core.presentation.base

interface StateConverter<T, U> {
    fun uiMap(state: T): U
}
