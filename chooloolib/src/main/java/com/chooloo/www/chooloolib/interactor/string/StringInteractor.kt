package com.chooloo.www.chooloolib.interactor.string

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface StringInteractor : BaseInteractor<StringInteractor.Listener> {
    interface Listener

    fun getString(@StringRes stringRes: Int): String
}