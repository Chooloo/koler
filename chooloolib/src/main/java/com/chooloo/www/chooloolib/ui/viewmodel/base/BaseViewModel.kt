package com.chooloo.www.chooloolib.ui.viewmodel.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _errorChannel = Channel<VMError>(Channel.BUFFERED)

    val errorFlow: Flow<VMError> = _errorChannel.receiveAsFlow()

    fun onError(error: VMError) {
        viewModelScope.launch {
            _errorChannel.send(error)
        }
    }

    open class VMError(@StringRes val strRes: Int? = null)
}
