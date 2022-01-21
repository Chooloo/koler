package com.chooloo.www.chooloolib.interactor.blocked

import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface BlockedInteractor : BaseInteractor<BlockedInteractor.Listener> {
    interface Listener

    fun blockNumber(number: String)
    fun unblockNumber(number: String)
    fun isNumberBlocked(number: String): Boolean
}