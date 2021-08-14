package com.chooloo.www.koler.interactor.numbers

import com.chooloo.www.koler.interactor.base.BaseInteractor

interface NumbersInteractor : BaseInteractor<NumbersInteractor.Listener> {
    interface Listener

    fun blockNumber(number: String)
    fun unblockNumber(number: String)
    fun isNumberBlocked(number: String): Boolean
}