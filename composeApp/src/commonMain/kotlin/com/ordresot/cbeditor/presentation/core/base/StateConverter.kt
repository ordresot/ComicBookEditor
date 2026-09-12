package com.ordresot.cbeditor.presentation.core.base

interface StateConverter<T, U> {
    fun uiMap(state: T): U
}
