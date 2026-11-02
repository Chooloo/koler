package com.chooloo.www.koler.service

import android.app.Notification
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.ServiceCompat
import com.chooloo.www.chooloolib.R as LibR
import com.chooloo.www.chooloolib.di.ApplicationScope
import com.chooloo.www.chooloolib.domain.model.IncomingCallMode
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.koler.R
import com.chooloo.www.koler.domain.AudioRoute
import com.chooloo.www.koler.domain.Call
import com.chooloo.www.koler.domain.CallAudio
import com.chooloo.www.koler.domain.CallRepository
import com.chooloo.www.koler.domain.CallState
import com.chooloo.www.koler.domain.primaryCall
import com.chooloo.www.koler.ui.call.CallActivity
import com.chooloo.www.koler.ui.call.labelRes
import com.chooloo.www.koler.ui.call.title
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@Singleton
class CallNotification @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val callRepository: CallRepository,
    private val phoneRepository: PhoneRepository,
    private val preferenceRepository: PreferenceRepository,
    private val notificationManager: NotificationManagerCompat,
) {
    private var job: Job? = null

    fun attach(service: Service) {
        createChannels()
        job?.cancel()
        job = applicationScope.launch(Dispatchers.Main.immediate) {
            combine(callRepository.calls, callRepository.audio, preferenceRepository.settings) { calls, audio, settings ->
                Triple(primaryCall(calls), audio, settings.incomingCallMode)
            }.collectLatest { (call, audio, incomingCallMode) ->
                if (call == null) {
                    ServiceCompat.stopForeground(service, ServiceCompat.STOP_FOREGROUND_REMOVE)
                } else {
                    ServiceCompat.startForeground(
                        service,
                        ID,
                        build(call, audio, incomingCallMode),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL
                    )
                }
            }
        }
    }

    fun detach(service: Service) {
        job?.cancel()
        job = null
        ServiceCompat.stopForeground(service, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private suspend fun build(call: Call, audio: CallAudio, incomingCallMode: IncomingCallMode): Notification {
        val caller = Person.Builder()
            .setName(call.title(context, call.number?.let { phoneRepository.lookup(it) }))
            .setImportant(true)
            .build()
        val isIncoming = call.state == CallState.INCOMING
        val style = if (isIncoming) {
            NotificationCompat.CallStyle.forIncomingCall(
                caller,
                receiverIntent(CallBroadcastReceiver.ACTION_DECLINE),
                receiverIntent(CallBroadcastReceiver.ACTION_ANSWER)
            )
        } else {
            NotificationCompat.CallStyle.forOngoingCall(caller, receiverIntent(CallBroadcastReceiver.ACTION_DECLINE))
        }

        return NotificationCompat.Builder(context, if (isIncoming) CHANNEL_ID_INCOMING else CHANNEL_ID_ONGOING)
            .setStyle(style)
            .setOngoing(true)
            .setOnlyAlertOnce(!isIncoming)
            .setCategory(Notification.CATEGORY_CALL)
            .setSmallIcon(R.drawable.ic_call)
            .setContentIntent(callScreenIntent())
            .setContentText(context.getString(call.state.labelRes))
            .apply {
                if (isIncoming && incomingCallMode == IncomingCallMode.FULL_SCREEN) {
                    setFullScreenIntent(callScreenIntent(), true)
                }
                if (call.hasStarted) {
                    setWhen(call.connectTimeMillis).setUsesChronometer(true)
                }
                if (!isIncoming) {
                    ongoingActions(call, audio).forEach(::addAction)
                }
            }
            .build()
    }

    private fun ongoingActions(call: Call, audio: CallAudio) = buildList {
        if (call.can(Call.Capability.MUTE)) {
            add(
                if (audio.isMuted) {
                    action(R.drawable.ic_mic, LibR.string.call_action_unmute, CallBroadcastReceiver.ACTION_MUTE_OFF)
                } else {
                    action(R.drawable.ic_mic_off, LibR.string.call_action_mute, CallBroadcastReceiver.ACTION_MUTE_ON)
                }
            )
        }
        add(
            if (audio.route == AudioRoute.SPEAKER) {
                action(R.drawable.ic_volume_down, LibR.string.call_action_speaker_off, CallBroadcastReceiver.ACTION_SPEAKER_OFF)
            } else {
                action(R.drawable.ic_volume_up, LibR.string.call_action_speaker, CallBroadcastReceiver.ACTION_SPEAKER_ON)
            }
        )
    }

    private fun action(iconRes: Int, titleRes: Int, receiverAction: String) =
        NotificationCompat.Action(iconRes, context.getString(titleRes), receiverIntent(receiverAction))

    private fun receiverIntent(receiverAction: String) = PendingIntent.getBroadcast(
        context,
        receiverAction.hashCode(),
        Intent(context, CallBroadcastReceiver::class.java).setAction(receiverAction),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun callScreenIntent() = PendingIntent.getActivity(
        context,
        0,
        Intent(context, CallActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun createChannels() {
        listOf(CHANNEL_ID_ONGOING to IMPORTANCE_DEFAULT, CHANNEL_ID_INCOMING to IMPORTANCE_HIGH)
            .forEach { (id, importance) ->
                notificationManager.createNotificationChannel(
                    NotificationChannelCompat.Builder(id, importance)
                        .setName(context.getString(LibR.string.call_notification_channel_name))
                        .setDescription(context.getString(LibR.string.call_notification_channel_description))
                        .build()
                )
            }
    }

    private companion object {
        const val ID = 420
        const val CHANNEL_ID_ONGOING = "cnc_priority_low"
        const val CHANNEL_ID_INCOMING = "cnc_priority_high"
    }
}
