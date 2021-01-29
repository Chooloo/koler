package com.chooloo.www.callmanager.entity

import android.provider.CallLog
import androidx.annotation.IntDef
import java.util.*

class RecentCall(
        val callId: Long? = null,
        val callerNumber: String? = null,
        val callDuration: String? = null,
        val callDate: Date? = null,
        val callType: Int? = null
) {

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(TYPE_OUTGOING, TYPE_INCOMING, TYPE_MISSED, TYPE_VOICEMAIL, TYPE_REJECTED)
    annotation class CallType

    companion object {
        const val TYPE_OUTGOING = CallLog.Calls.OUTGOING_TYPE
        const val TYPE_INCOMING = CallLog.Calls.INCOMING_TYPE
        const val TYPE_MISSED = CallLog.Calls.MISSED_TYPE
        const val TYPE_VOICEMAIL = CallLog.Calls.VOICEMAIL_TYPE
        const val TYPE_REJECTED = CallLog.Calls.REJECTED_TYPE

        val UNKNOWN = RecentCall(callerNumber = "Unknown")
    }

}