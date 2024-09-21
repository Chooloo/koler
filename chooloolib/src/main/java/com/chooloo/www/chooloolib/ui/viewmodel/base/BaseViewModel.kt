package com.chooloo.www.chooloolib.ui.viewmodel.base

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.Flow

interface BaseViewModel {
    val errorFlow: Flow<VMError>

    fun onError(error: VMError)

    open class VMError(@StringRes val strRes: Int? = null)
}