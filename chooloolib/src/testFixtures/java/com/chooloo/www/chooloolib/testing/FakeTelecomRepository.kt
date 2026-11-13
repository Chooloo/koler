package com.chooloo.www.chooloolib.testing

import com.chooloo.www.chooloolib.domain.repository.TelecomRepository

class FakeTelecomRepository(private val defaultDialer: Boolean = true) : TelecomRepository {
    val calledNumbers = mutableListOf<String>()
    val secretCodes = mutableListOf<String>()
    var voicemailCalls = 0
    var missedCallsNotificationCancels = 0
    var callError: Exception? = null
    var secretCodeError: Exception? = null

    override fun isDefaultDialer() = defaultDialer

    override suspend fun callNumber(number: String) {
        callError?.let { throw it }
        calledNumbers += number
    }

    override suspend fun callVoicemail() {
        voicemailCalls++
    }

    override suspend fun handleSecretCode(code: String) {
        secretCodeError?.let { throw it }
        secretCodes += code
    }

    override suspend fun cancelMissedCallsNotification() {
        missedCallsNotificationCancels++
    }
}
