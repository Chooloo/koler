package com.chooloo.www.chooloolib.interactor.call

import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import com.chooloo.www.chooloolib.model.SimAccount

interface CallNavigationsInteractor : BaseInteractor<CallNavigationsInteractor.Listener> {
    interface Listener

    fun callVoicemail()
    fun call(number: String)
    fun call(simAccount: SimAccount?, number: String)
}