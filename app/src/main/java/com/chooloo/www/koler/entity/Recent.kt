package com.chooloo.www.koler.entity

import android.provider.CallLog
import androidx.annotation.IntDef
import com.chooloo.www.koler.util.RelativeTime.getTimeAgo
import java.io.Serializable
import java.util.*

data class Recent(
    val number: String,
    @CallType val type: Int,
    val id: Long = 0,
    val duration: String? = null,
    val date: Date? = null,
    val cachedName: String? = null,
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

        val UNKNOWN = Recent(number = "Unknown", type = TYPE_UNKNOWN)
    }

    val relativeTime: String?
        get() = date?.time?.let { getTimeAgo(it) }

}