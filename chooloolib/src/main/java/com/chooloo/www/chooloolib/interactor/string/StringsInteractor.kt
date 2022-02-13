package com.chooloo.www.chooloolib.interactor.string

import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface StringsInteractor : BaseInteractor<StringsInteractor.Listener> {
    interface Listener

    fun getString(@StringRes stringRes: Int): String
}