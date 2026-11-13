package com.chooloo.www.koler.notification

import android.app.Notification
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.telecom.PhoneAccount
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.ServiceCompat
import com.chooloo.www.chooloolib.R as LibR
import com.chooloo.www.chooloolib.di.ApplicationScope
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.koler.AppConstants
import com.chooloo.www.koler.R
import com.chooloo.www.koler.domain.enum.AudioRoute
import com.chooloo.www.koler.domain.enum.CallCapability
import com.chooloo.www.koler.domain.enum.CallState
import com.chooloo.www.koler.domain.model.Call
import com.chooloo.www.koler.domain.model.CallAudio
import com.chooloo.www.koler.domain.repository.CallRepository
import com.chooloo.www.koler.domain.usecase.GetPrimaryCall
import com.chooloo.www.koler.receiver.CallActionReceiver
import com.chooloo.www.koler.ui.call.CallActivity
import com.chooloo.www.koler.ui.common.CallLabels
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
    private val notificationManager: NotificationManagerCompat,
    private val getPrimaryCall: GetPrimaryCall,
) {
    private var job: Job? = null
    private var alertedCallId: String? = null

    fun attach(service: Service) {
        createChannels()
        job?.cancel()
        job = applicationScope.launch(Dispatchers.Main.immediate) {
            combine(callRepository.calls, callRepository.audio) { calls, audio ->
                getPrimaryCall(calls) to audio
            }.collectLatest { (call, audio) ->
                if (call == null) {
                    ServiceCompat.stopForeground(service, ServiceCompat.STOP_FOREGROUND_REMOVE)
                } else {
                    ServiceCompat.startForeground(
                        service,
                        AppConstants.CALL_NOTIFICATION_ID,
                        build(call, audio),
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL,
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

    private suspend fun build(call: Call, audio: CallAudio): Notification {
        val caller = Person.Builder()
            .setName(
                CallLabels.title(call, context, call.number?.let { phoneRepository.lookup(it) }),
            )
            .setImportant(true)
            .setUri(
                call.number
                    ?.takeIf { it.isNotBlank() }
                    ?.let { Uri.fromParts(PhoneAccount.SCHEME_TEL, it, null).toString() },
            )
            .build()
        val isIncoming = call.state == CallState.INCOMING
        val shouldAlert = isIncoming && call.id != alertedCallId
        if (shouldAlert) alertedCallId = call.id
        val style = if (isIncoming) {
            NotificationCompat.CallStyle.forIncomingCall(
                caller,
                receiverIntent(AppConstants.ACTION_DECLINE),
                receiverIntent(AppConstants.ACTION_ANSWER),
            )
        } else {
            NotificationCompat.CallStyle.forOngoingCall(
                caller,
                receiverIntent(AppConstants.ACTION_DECLINE),
            )
        }

        val channelId = if (isIncoming) {
            AppConstants.CHANNEL_ID_CALL_INCOMING
        } else {
            AppConstants.CHANNEL_ID_CALL_ONGOING
        }
        return NotificationCompat.Builder(context, channelId)
            .setStyle(style)
            .addPerson(caller)
            .setOngoing(true)
            .setOnlyAlertOnce(!shouldAlert)
            .setCategory(Notification.CATEGORY_CALL)
            .setSmallIcon(R.drawable.ic_call)
            .setContentIntent(callScreenIntent())
            .setContentText(context.getString(CallLabels.labelRes(call.state)))
            .apply {
                if (isIncoming) {
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
        if (call.can(CallCapability.MUTE)) {
            add(
                if (audio.isMuted) {
                    action(
                        R.drawable.ic_mic_off,
                        LibR.string.call_action_unmute,
                        AppConstants.ACTION_MUTE_OFF,
                    )
                } else {
                    action(
                        R.drawable.ic_mic_off,
                        LibR.string.call_action_mute,
                        AppConstants.ACTION_MUTE_ON,
                    )
                },
            )
        }
        add(
            if (audio.route == AudioRoute.SPEAKER) {
                action(
                    R.drawable.ic_volume_up,
                    LibR.string.call_action_speaker_off,
                    AppConstants.ACTION_SPEAKER_OFF,
                )
            } else {
                action(
                    R.drawable.ic_volume_up,
                    LibR.string.call_action_speaker,
                    AppConstants.ACTION_SPEAKER_ON,
                )
            },
        )
    }

    private fun action(iconRes: Int, titleRes: Int, receiverAction: String) =
        NotificationCompat.Action(
            iconRes,
            context.getString(titleRes),
            receiverIntent(receiverAction),
        )

    private fun receiverIntent(receiverAction: String) = PendingIntent.getBroadcast(
        context,
        receiverAction.hashCode(),
        Intent(context, CallActionReceiver::class.java).setAction(receiverAction),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    private fun callScreenIntent() = PendingIntent.getActivity(
        context,
        0,
        Intent(context, CallActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )

    private fun createChannels() {
        AppConstants.LEGACY_CALL_CHANNEL_IDS.forEach(notificationManager::deleteNotificationChannel)
        notificationManager.createNotificationChannelsCompat(
            listOf(
                channel(
                    AppConstants.CHANNEL_ID_CALL_ONGOING,
                    IMPORTANCE_DEFAULT,
                    LibR.string.call_notification_channel_ongoing_name,
                    LibR.string.call_notification_channel_ongoing_description,
                ).build(),
                // Telecom plays the ringtone because the manifest doesn't declare IN_CALL_SERVICE_RINGING.
                channel(
                    AppConstants.CHANNEL_ID_CALL_INCOMING,
                    IMPORTANCE_HIGH,
                    LibR.string.call_notification_channel_incoming_name,
                    LibR.string.call_notification_channel_incoming_description,
                )
                    .setSound(null, null)
                    .build(),
            ),
        )
    }

    private fun channel(id: String, importance: Int, nameRes: Int, descriptionRes: Int) =
        NotificationChannelCompat.Builder(id, importance)
            .setName(context.getString(nameRes))
            .setDescription(context.getString(descriptionRes))
}
