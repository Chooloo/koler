package com.chooloo.www.chooloolib.domain.repository.telecom

import android.net.Uri
import com.chooloo.www.chooloolib.domain.model.CallData

interface TelecomRepository {
    suspend fun getCallUri(number: String): Uri
    suspend fun handleMmi(code: String): Boolean
    suspend fun handleSecretCode(code: String): Boolean
    suspend fun handleSpecialChars(code: String): Boolean

    suspend fun callVoicemail()
    suspend fun isCallEmergency(call: CallData): Boolean
    suspend fun callNumber(number: String)
}
