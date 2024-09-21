package com.chooloo.www.chooloolib.ui.notification

import android.app.Notification
import android.app.Notification.CATEGORY_CALL
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_ANSWER
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.drawable.Icon
import android.os.Build
import android.telecom.Call.Details.CAPABILITY_MUTE
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.di.module.IoScope
import com.chooloo.www.chooloolib.domain.model.CallData
import com.chooloo.www.chooloolib.domain.model.CallState
import com.chooloo.www.chooloolib.domain.receiver.CallBroadcastReceiver
import com.chooloo.www.chooloolib.domain.receiver.CallBroadcastReceiver.Companion.ACTION_DECLINE
import com.chooloo.www.chooloolib.domain.receiver.CallBroadcastReceiver.Companion.ACTION_MUTE_OFF
import com.chooloo.www.chooloolib.domain.receiver.CallBroadcastReceiver.Companion.ACTION_MUTE_ON
import com.chooloo.www.chooloolib.domain.receiver.CallBroadcastReceiver.Companion.ACTION_SPEAKER_OFF
import com.chooloo.www.chooloolib.domain.receiver.CallBroadcastReceiver.Companion.ACTION_SPEAKER_ON
import com.chooloo.www.chooloolib.domain.repository.call.CallRepository
import com.chooloo.www.chooloolib.domain.repository.color.ColorRepository
import com.chooloo.www.chooloolib.domain.repository.notification.NotificationRepository
import com.chooloo.www.chooloolib.domain.repository.notification.NotificationRepository.Companion.Priority
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.ui.activity.CallActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@RequiresApi(Build.VERSION_CODES.O)
class CallNotification @Inject constructor(
    private val callRepository: CallRepository,
    private val colorRepository: ColorRepository,
    private val phoneRepository: PhoneRepository,
    @IoScope private val ioScope: CoroutineScope,
    @ApplicationContext private val context: Context,
    private val preferenceRepository: PreferenceRepository,
    private val notificationRepository: NotificationRepository,
) {
    private var _callData: CallData? = null

    private val _channel
        get() = if (_callData?.state == CallState.INCOMING && preferenceRepository.incomingCallMode.value == IncomingCallMode.POP_UP) {
            _channelImportanceHigh
        } else {
            _channelImportanceLow
        }

    private val _channelImportanceLow by lazy {
        notificationRepository.createNotificationChannel(
            id = CHANNEL_ID_PRIORITY_LOW,
            importance = IMPORTANCE_DEFAULT,
            name = getString(R.string.call_notification_channel_name),
            description = getString(R.string.call_notification_channel_description)
        )
    }

    private val _channelImportanceHigh by lazy {
        notificationRepository.createNotificationChannel(
            id = CHANNEL_ID_PRIORITY_HIGH,
            importance = IMPORTANCE_HIGH,
            name = getString(R.string.call_notification_channel_name),
            description = getString(R.string.call_notification_channel_description)
        )
    }

    private val _contentPendingIntent by lazy {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, CallActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            },
            FLAG_IMMUTABLE
        )
    }

    private val _actionAnswer by lazy {
        NotificationCompat.Action(
//            IconCompat.createFromIcon(Icon(Icons.AutoMirrored.Outlined.CallMade)),
            IconCompat.createFromIcon(
                context,
                Icon.createWithResource(context, R.drawable.view_carousel)
            ),
            getString(R.string.action_answer),
            getCallPendingIntent(ACTION_ANSWER, 0)
        )
    }

    private val _actionDecline by lazy {
        NotificationCompat.Action(
//            IconCompat.createFromIcon(Icon(Icons.Outlined.CallEnd)),
            IconCompat.createFromIcon(
                context,
                Icon.createWithResource(context, R.drawable.view_carousel)
            ),
            getString(R.string.action_hangup),
            getCallPendingIntent(ACTION_DECLINE, 1)
        )
    }

    private val _actionMuteOn by lazy {
        NotificationCompat.Action(
//            IconCompat.createFromIcon(Icon(Icons.Outlined.Mic)),
            IconCompat.createFromIcon(
                context,
                Icon.createWithResource(context, R.drawable.view_carousel)
            ),
            getString(R.string.call_action_mute),
            getCallPendingIntent(ACTION_MUTE_ON, 2)
        )
    }

    private val _actionMuteOff by lazy {
        NotificationCompat.Action(
//            IconCompat.createFromIcon(Icon(Icons.Outlined.MicOff)),
            IconCompat.createFromIcon(
                context,
                Icon.createWithResource(context, R.drawable.view_carousel)
            ),
            getString(R.string.call_action_unmute),
            getCallPendingIntent(ACTION_MUTE_OFF, 3)
        )
    }

    private val _actionSpeakerOn by lazy {
        NotificationCompat.Action(
//            IconCompat.createFromIcon(Icon.(Icons.AutoMirrored.Outlined.VolumeDown)),
            IconCompat.createFromIcon(
                context,
                Icon.createWithResource(context, R.drawable.view_carousel)
            ),
            getString(R.string.call_action_speaker),
            getCallPendingIntent(ACTION_SPEAKER_ON, 4)
        )
    }

    private val _actionSpeakerOff by lazy {
        NotificationCompat.Action(
//            IconCompat.createFromIcon(Icon(Icons.AutoMirrored.Outlined.VolumeUp)),
            IconCompat.createFromIcon(
                context,
                Icon.createWithResource(context, R.drawable.view_carousel)
            ),
            getString(R.string.call_action_speaker_off),
            getCallPendingIntent(ACTION_SPEAKER_OFF, 5)
        )
    }

    private fun getCallIntent(callAction: String) =
        Intent(context, CallBroadcastReceiver::class.java).apply {
            action = callAction
            putExtra(EXTRA_NOTIFICATION_ID, ID)
        }

    private fun getCallPendingIntent(callAction: String, rc: Int) =
        PendingIntent.getBroadcast(
            context,
            rc,
            getCallIntent(callAction),
            FLAG_CANCEL_CURRENT or FLAG_IMMUTABLE
        )

    private suspend fun buildNotification(callData: CallData): Notification {
        val phoneAccount = phoneRepository.lookupAccount(callData.number)
        val actions = mutableListOf<NotificationCompat.Action>()

        if (callData.state !in arrayOf(CallState.DISCONNECTED, CallState.DISCONNECTING)) {
            actions.add(_actionDecline)
        }

        if (callData.state == CallState.INCOMING) {
            actions.add(_actionAnswer)
        } else if (callData.state !in arrayOf(CallState.DISCONNECTED, CallState.DISCONNECTING)) {
            callRepository.isMuteActive.value?.let { isMuted ->
                if (callData.isCapable(CAPABILITY_MUTE)) {
                    actions.add(if (isMuted) _actionMuteOff else _actionMuteOn)
                }
            }

            callRepository.isSpeakerActive.value?.let { isSpeakerOn ->
                actions.add(if (isSpeakerOn) _actionSpeakerOff else _actionSpeakerOn)
            }
        }

        return notificationRepository.createNotification(
            isOngoing = true,
            actions = actions,
            priority = Priority.HIGH,
            category = CATEGORY_CALL,
            contentIntent = _contentPendingIntent,
            color = colorRepository.getAttrColor(R.attr.colorSecondary),
            contentTitle = phoneAccount?.displayString ?: callData.number,
            contentText = ContextCompat.getString(context, callData.state.stringRes)
        )
    }

    private fun getString(@StringRes res: Int) =
        context.getString(res)

    private suspend fun show(callData: CallData) {
        val notification = buildNotification(callData)
        notificationRepository.showNotification(ID, notification)
    }

    private suspend fun refresh() {
        _callData?.let { show(it) }
    }

    fun attach() {
        callRepository.apply {
            ioScope.launch {
                audioRoute.collect { refresh() }
            }

            ioScope.launch {
                isMuteActive.collect { refresh() }
            }

            ioScope.launch {
                mainCall.collect {
                    _callData = it
                    if (it == null) {
                        detach()
                    } else {
                        refresh()
                    }
                }
            }
        }

        Thread.currentThread().setUncaughtExceptionHandler { _, _ -> detach() }
    }

    fun detach() {
        notificationRepository.cancelNotification(ID)
    }


    companion object {
        const val ID = 420
        const val PRIORITY = NotificationCompat.PRIORITY_HIGH
        const val CHANNEL_ID_PRIORITY_LOW = "cnc_priority_low"
        const val CHANNEL_ID_PRIORITY_HIGH = "cnc_priority_high"
    }
}