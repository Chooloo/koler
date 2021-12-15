package com.chooloo.www.koler.interactor.blocked

import com.chooloo.www.koler.interactor.base.BaseInteractor

interface BlockedInteractor : BaseInteractor<BlockedInteractor.Listener> {
    interface Listener

    fun blockNumber(number: String)
    fun unblockNumber(number: String)
    fun isNumberBlocked(number: String): Boolean
}