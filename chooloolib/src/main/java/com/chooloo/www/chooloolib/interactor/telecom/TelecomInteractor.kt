package com.chooloo.www.chooloolib.interactor.telecom

import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import com.chooloo.www.chooloolib.data.model.SimAccount

interface TelecomInteractor : BaseInteractor<TelecomInteractor.Listener> {
    interface Listener {}

    fun handleMmi(code: String): Boolean
    fun handleSecretCode(code: String): Boolean
    fun handleSpecialChars(code: String): Boolean
    fun callVoicemail()
    fun callNumber(number: String, simAccount: SimAccount? = null)
}