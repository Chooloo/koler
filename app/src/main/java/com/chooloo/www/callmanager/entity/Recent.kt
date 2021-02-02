package com.chooloo.www.callmanager.entity

import android.provider.CallLog
import androidx.annotation.IntDef
import java.io.Serializable
import java.util.*

data class Recent(
        val callerNumber: String,
        @CallType val callType: Int,
        val callId: Long? = null,
        val callDuration: String? = null,
        val callDate: Date? = null,
) : Serializable {

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(TYPE_INCOMING, TYPE_OUTGOING, TYPE_MISSED, TYPE_VOICEMAIL, TYPE_REJECTED, TYPE_UNKNOWN)
    annotation class CallType

    companion object {
        const val TYPE_INCOMING = CallLog.Calls.INCOMING_TYPE
        const val TYPE_OUTGOING = CallLog.Calls.OUTGOING_TYPE
        const val TYPE_MISSED = CallLog.Calls.MISSED_TYPE
        const val TYPE_VOICEMAIL = CallLog.Calls.VOICEMAIL_TYPE
        const val TYPE_REJECTED = CallLog.Calls.REJECTED_TYPE
        const val TYPE_UNKNOWN = 6

        val UNKNOWN = Recent(callerNumber = "Unknown", callType = TYPE_UNKNOWN)
    }
}