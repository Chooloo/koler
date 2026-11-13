package com.chooloo.www.chooloolib.ui.home

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow

class HomeSearch(private val savedStateHandle: SavedStateHandle) {
    val isOpen: StateFlow<Boolean> = savedStateHandle.getStateFlow(KEY_OPEN, false)
    val text: StateFlow<String> = savedStateHandle.getStateFlow(KEY_TEXT, "")

    fun open() {
        savedStateHandle[KEY_OPEN] = true
    }

    fun close() {
        savedStateHandle[KEY_OPEN] = false
        savedStateHandle[KEY_TEXT] = ""
    }

    fun changeText(text: String) {
        savedStateHandle[KEY_TEXT] = text
    }

    private companion object {
        const val KEY_OPEN = "searching"
        const val KEY_TEXT = "search_text"
    }
}
