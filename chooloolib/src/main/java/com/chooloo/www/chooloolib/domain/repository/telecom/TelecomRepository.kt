package com.chooloo.www.chooloolib.domain.repository.telecom

import android.net.Uri

interface TelecomRepository {
    suspend fun getCallUri(number: String): Uri
    suspend fun handleMmi(code: String): Boolean
    suspend fun handleSecretCode(code: String): Boolean
    suspend fun handleSpecialChars(code: String): Boolean

    suspend fun callVoicemail()
    suspend fun callNumber(number: String)
}
