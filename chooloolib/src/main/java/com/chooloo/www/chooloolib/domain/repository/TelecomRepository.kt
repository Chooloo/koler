package com.chooloo.www.chooloolib.domain.repository

interface TelecomRepository {
    fun isDefaultDialer(): Boolean
    suspend fun callNumber(number: String)
    suspend fun callVoicemail()
    suspend fun handleSecretCode(code: String): Boolean
}
