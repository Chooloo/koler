package com.chooloo.www.koler.interactor.string

import androidx.annotation.StringRes
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface StringInteractor :BaseInteractor<StringInteractor.Listener>{
    interface Listener

    fun getString(@StringRes stringRes: Int): String
}