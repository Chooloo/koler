package com.chooloo.www.chooloolib.interactor.blocked

import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface BlockedInteractor : BaseInteractor<BlockedInteractor.Listener> {
    interface Listener

    suspend fun blockNumber(number: String)
    suspend fun unblockNumber(number: String)
    suspend fun isNumberBlocked(number: String): Boolean
}