package com.chooloo.www.koler.testcall

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.DisconnectCause
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log

class TestConnectionService : ConnectionService() {
    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest,
    ): Connection {
        val number = request.extras.getString(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS)
            ?: error("Incoming test call has no number")
        return TestConnection().apply {
            setAddress(telUri(number), TelecomManager.PRESENTATION_ALLOWED)
            connectionCapabilities = Connection.CAPABILITY_HOLD or
                Connection.CAPABILITY_SUPPORT_HOLD or
                Connection.CAPABILITY_MUTE
            setRinging()
            connections.add(this)
            Log.i(TAG, "Ringing test call from $number")
        }
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?,
    ) {
        Log.e(TAG, "Telecom refused the incoming test call")
    }

    private class TestConnection : Connection() {
        override fun onAnswer() = setActive()

        override fun onHold() = setOnHold()

        override fun onUnhold() = setActive()

        override fun onReject() = end(DisconnectCause.REJECTED)

        override fun onDisconnect() = end(DisconnectCause.LOCAL)

        override fun onAbort() = end(DisconnectCause.CANCELED)

        fun end(code: Int) {
            setDisconnected(DisconnectCause(code))
            connections.remove(this)
            destroy()
        }
    }

    companion object {
        private const val TAG = "KolerTestCall"
        private const val ACCOUNT_ID = "koler_test"

        // Telecom calls ConnectionService and the receiver on the main thread only.
        private val connections = mutableListOf<TestConnection>()

        fun accountHandle(context: Context) = PhoneAccountHandle(
            ComponentName(context, TestConnectionService::class.java),
            ACCOUNT_ID,
        )

        fun updateRinging(number: String) {
            val ringing = connections.lastOrNull { it.state == Connection.STATE_RINGING }
                ?: error("No ringing test call to update")
            ringing.setAddress(telUri(number), TelecomManager.PRESENTATION_ALLOWED)
        }

        fun hangupAll() {
            connections.toList().forEach {
                it.end(
                    if (it.state == Connection.STATE_RINGING) {
                        DisconnectCause.MISSED
                    } else {
                        DisconnectCause.REMOTE
                    },
                )
            }
        }

        private fun telUri(number: String): Uri =
            Uri.fromParts(PhoneAccount.SCHEME_TEL, number, null)
    }
}
