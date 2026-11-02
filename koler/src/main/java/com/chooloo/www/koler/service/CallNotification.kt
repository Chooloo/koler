package com.chooloo.www.koler.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.di.ApplicationScope
import com.chooloo.www.chooloolib.domain.model.IncomingCallMode
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.koler.domain.AudioRoute
import com.chooloo.www.koler.domain.Call
import com.chooloo.www.koler.domain.CallAudio
import com.chooloo.www.koler.domain.CallRepository
import com.chooloo.www.koler.domain.CallState
import com.chooloo.www.koler.domain.primaryCall
import com.chooloo.www.koler.ui.call.CallActivity
import com.chooloo.www.koler.ui.call.labelRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
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

    fun attach() {
        createChannels()
        job?.cancel()
        job = applicationScope.launch {
            combine(callRepository.calls, callRepository.audio, preferenceRepository.settings) { calls, audio, settings ->
                Triple(primaryCall(calls), audio, settings.incomingCallMode)
            }.collectLatest { (call, audio, incomingCallMode) ->
                if (call == null) notificationManager.cancel(ID) else show(call, audio, incomingCallMode)
            }
        }
    }

    fun detach() {
        job?.cancel()
        job = null
        notificationManager.cancel(ID)
    }

    @SuppressLint("MissingPermission")
    private suspend fun show(call: Call, audio: CallAudio, incomingCallMode: IncomingCallMode) {
        val title = call.number?.let { phoneRepository.lookup(it)?.name } ?: call.number
            ?: context.getString(R.string.caller_unknown)
        val isIncoming = call.state == CallState.INCOMING
        val isPopUp = incomingCallMode == IncomingCallMode.POP_UP
        val channelId = if (isIncoming && isPopUp) CHANNEL_ID_PRIORITY_HIGH else CHANNEL_ID_PRIORITY_LOW

        val builder = NotificationCompat.Builder(context, channelId)
            .setWhen(0)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setCategory(Notification.CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.view_carousel)
            .setContentIntent(contentIntent())
            .setContentTitle(title)
            .setContentText(context.getString(call.state.labelRes))

        actionsFor(call, audio).forEach(builder::addAction)
        notificationManager.notify(ID, builder.build())
    }

    private fun actionsFor(call: Call, audio: CallAudio): List<NotificationCompat.Action> {
        if (call.state in setOf(CallState.DISCONNECTED, CallState.DISCONNECTING)) return emptyList()
        if (call.state == CallState.INCOMING) {
            return listOf(
                action(R.string.action_hangup, CallBroadcastReceiver.ACTION_DECLINE),
                action(R.string.action_answer, CallBroadcastReceiver.ACTION_ANSWER),
            )
        }
        return buildList {
            add(action(R.string.action_hangup, CallBroadcastReceiver.ACTION_DECLINE))
            if (call.can(Call.Capability.MUTE)) {
                add(
                    if (audio.isMuted) action(R.string.call_action_unmute, CallBroadcastReceiver.ACTION_MUTE_OFF)
                    else action(R.string.call_action_mute, CallBroadcastReceiver.ACTION_MUTE_ON)
                )
            }
            add(
                if (audio.route == AudioRoute.SPEAKER) action(R.string.call_action_speaker_off, CallBroadcastReceiver.ACTION_SPEAKER_OFF)
                else action(R.string.call_action_speaker, CallBroadcastReceiver.ACTION_SPEAKER_ON)
            )
        }
    }

    private fun action(titleRes: Int, receiverAction: String) = NotificationCompat.Action(
        R.drawable.view_carousel,
        context.getString(titleRes),
        PendingIntent.getBroadcast(
            context,
            receiverAction.hashCode(),
            Intent(context, CallBroadcastReceiver::class.java).setAction(receiverAction),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    )

    private fun contentIntent() = PendingIntent.getActivity(
        context,
        0,
        Intent(context, CallActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        PendingIntent.FLAG_IMMUTABLE
    )

    private fun createChannels() {
        listOf(CHANNEL_ID_PRIORITY_LOW to IMPORTANCE_DEFAULT, CHANNEL_ID_PRIORITY_HIGH to IMPORTANCE_HIGH)
            .forEach { (id, importance) ->
                notificationManager.createNotificationChannel(
                    NotificationChannelCompat.Builder(id, importance)
                        .setName(context.getString(R.string.call_notification_channel_name))
                        .setDescription(context.getString(R.string.call_notification_channel_description))
                        .build()
                )
            }
    }

    private companion object {
        const val ID = 420
        const val CHANNEL_ID_PRIORITY_LOW = "cnc_priority_low"
        const val CHANNEL_ID_PRIORITY_HIGH = "cnc_priority_high"
    }
}
