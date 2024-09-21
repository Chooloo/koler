package com.chooloo.www.chooloolib.domain.model.record

import android.provider.CallLog
import androidx.annotation.IntDef
import com.chooloo.www.chooloolib.domain.model.RecentData
import java.io.Serializable
import java.util.Date

data class RecentRecord(
    val date: Date,
    val id: Long = 0,
    val number: String,
    @CallType val type: Int,
    val duration: Long = 0,
    val cachedName: String? = null,
    val groupAccounts: List<RecentData> = listOf()
) : Serializable {
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        TYPE_INCOMING,
        TYPE_OUTGOING,
        TYPE_MISSED,
        TYPE_BLOCKED,
        TYPE_VOICEMAIL,
        TYPE_REJECTED,
        TYPE_UNKNOWN
    )
    annotation class CallType

    companion object {
        const val TYPE_UNKNOWN = 7
        const val TYPE_MISSED = CallLog.Calls.MISSED_TYPE
        const val TYPE_BLOCKED = CallLog.Calls.BLOCKED_TYPE
        const val TYPE_INCOMING = CallLog.Calls.INCOMING_TYPE
        const val TYPE_OUTGOING = CallLog.Calls.OUTGOING_TYPE
        const val TYPE_REJECTED = CallLog.Calls.REJECTED_TYPE
        const val TYPE_VOICEMAIL = CallLog.Calls.VOICEMAIL_TYPE
    }
}