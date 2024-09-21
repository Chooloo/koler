package com.chooloo.www.chooloolib.ui.viewmodel.keypad

import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.StateFlow


interface KeypadViewModel : BaseViewModel {
    companion object {
        data class UiState(
            val value: String
        )
    }

    val uiState: StateFlow<UiState>

    fun onValueClick(value: String)
}