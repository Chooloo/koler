package com.chooloo.www.chooloolib.ui.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel.VMError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModelImpl : ViewModel(), BaseViewModel {
    private val _errorChannel = Channel<VMError>(Channel.BUFFERED)

    override val errorFlow = _errorChannel.receiveAsFlow()

    override fun onError(error: VMError) {
        viewModelScope.launch {
            _errorChannel.send(error)
        }
    }
}