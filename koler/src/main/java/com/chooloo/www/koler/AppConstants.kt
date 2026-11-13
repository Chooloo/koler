package com.chooloo.www.koler

object AppConstants {
    const val ACTION_SHOW_MISSED_CALLS = "com.chooloo.www.koler.action.SHOW_MISSED_CALLS"

    const val ACTION_ANSWER = "action_answer"
    const val ACTION_DECLINE = "action_decline"
    const val ACTION_MUTE_ON = "action_mute_on"
    const val ACTION_MUTE_OFF = "action_mute_off"
    const val ACTION_SPEAKER_ON = "action_speaker_on"
    const val ACTION_SPEAKER_OFF = "action_speaker_off"

    const val CALL_NOTIFICATION_ID = 420
    const val MISSED_CALL_NOTIFICATION_ID = 421

    const val CHANNEL_ID_CALL_ONGOING = "cnc_priority_low"
    const val CHANNEL_ID_CALL_INCOMING = "call_incoming"
    const val CHANNEL_ID_MISSED_CALLS = "missed_calls"
    val LEGACY_CALL_CHANNEL_IDS = listOf("cnc_priority_high", "call_notification_channel")
}
