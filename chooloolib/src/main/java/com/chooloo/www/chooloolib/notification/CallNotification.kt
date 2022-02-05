package com.chooloo.www.chooloolib.notification

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Build
import android.telecom.Call.Details.CAPABILITY_MUTE
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.chooloolib.data.call.Call.State.DISCONNECTED
import com.chooloo.www.chooloolib.data.call.Call.State.DISCONNECTING
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver.Companion.ACTION_HANGUP
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver.Companion.ACTION_MUTE
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver.Companion.ACTION_SPEAKER
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver.Companion.ACTION_UNMUTE
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver.Companion.ACTION_UNSPEAKER
import com.chooloo.www.chooloolib.ui.call.CallActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@RequiresApi(Build.VERSION_CODES.O)
class CallNotification @Inject constructor(
    @ApplicationContext private val context: Context,
    private val callsInteractor: CallsInteractor,
    private val colorsInteractor: ColorsInteractor,
    private val phonesInteractor: PhonesInteractor,
    private val stringsInteractor: StringsInteractor,
    private val callAudiosInteractor: CallAudiosInteractor,
    private val notificationManager: NotificationManagerCompat
) : CallsInteractor.Listener, CallAudiosInteractor.Listener {
    private var _call: Call? = null


    override fun onNoCalls() {
        detach()
        cancel()
    }

    override fun onCallChanged(call: Call) {
    }

    override fun onMainCallChanged(call: Call) {
        show(call)
        _call = call
    }


    override fun onMuteChanged(isMuted: Boolean) {
        _call?.let { show(it) }
    }

    override fun onAudioRouteChanged(audiosRoute: CallAudiosInteractor.AudioRoute) {
        _call?.let { show(it) }
    }


    fun attach() {
        createNotificationChannel()
        callsInteractor.registerListener(this)
        callAudiosInteractor.registerListener(this)
    }

    fun detach() {
        callsInteractor.unregisterListener(this)
        callAudiosInteractor.unregisterListener(this)
        cancel()
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

    private val _answerAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_call_black_24dp,
            stringsInteractor.getString(R.string.action_answer),
            getCallPendingIntent(ACTION_ANSWER, 0)
        )
    }

    private val _hangupAction by lazy {
        NotificationCompat.Action(
            R.drawable.round_call_end_24,
            stringsInteractor.getString(R.string.action_hangup),
            getCallPendingIntent(ACTION_HANGUP, 1)
        )
    }

    private val _muteAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_mic_black_24dp,
            stringsInteractor.getString(R.string.call_action_mute),
            getCallPendingIntent(ACTION_MUTE, 2)
        )
    }

    private val _unmuteAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_mic_off_black_24dp,
            stringsInteractor.getString(R.string.call_action_unmute),
            getCallPendingIntent(ACTION_UNMUTE, 3)
        )
    }

    private val _speakerAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_volume_down_black_24dp,
            stringsInteractor.getString(R.string.call_action_speaker),
            getCallPendingIntent(ACTION_SPEAKER, 4)
        )
    }

    private val _unspeakerAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_volume_up_black_24dp,
            stringsInteractor.getString(R.string.call_action_speaker_off),
            getCallPendingIntent(ACTION_UNSPEAKER, 5)
        )
    }

    private val _channel by lazy {
        NotificationChannelCompat.Builder(CHANNEL_ID, IMPORTANCE_HIGH)
            .setName(stringsInteractor.getString(R.string.call_notification_channel_name))
            .setDescription(stringsInteractor.getString(R.string.call_notification_channel_description))
            .setLightsEnabled(true)
            .build()
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


    private fun buildNotification(call: Call, callback: (Notification) -> Unit) {
        phonesInteractor.lookupAccount(call.number) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setWhen(0)
                .setOngoing(true)
                .setColorized(true)
                .setPriority(PRIORITY)
                .setOnlyAlertOnce(true)
                .setContentTitle(it?.displayString ?: call.number)
                .setSmallIcon(R.drawable.icon_full_144)
                .setContentIntent(_contentPendingIntent)
                .setColor(colorsInteractor.getAttrColor(R.attr.colorSecondary))
                .setContentText(stringsInteractor.getString(call.state.stringRes))
            if (call.isIncoming) {
                builder.addAction(_answerAction)
            }
            if (call.state !in arrayOf(DISCONNECTED, DISCONNECTING)) {
                builder.addAction(_hangupAction)
            }
            callAudiosInteractor.isMuted?.let { isMuted ->
                if (call.isCapable(CAPABILITY_MUTE)) {
                    builder.addAction(if (isMuted) _unmuteAction else _muteAction)
                }
            }
            callAudiosInteractor.isSpeakerOn?.let { isSpeakerOn ->
                builder.addAction(if (isSpeakerOn) _unspeakerAction else _speakerAction)
            }
            callback.invoke(builder.build())
        }
    }


    fun show(call: Call) {
        buildNotification(call) {
            notificationManager.notify(ID, it)
        }
    }

    fun cancel() {
        notificationManager.cancel(ID)
    }

    fun createNotificationChannel() {
        notificationManager.createNotificationChannel(_channel)
    }

    companion object {
        const val ID = 420
        const val CHANNEL_ID = "call_notification_channel"
        const val PRIORITY = NotificationCompat.PRIORITY_LOW
    }
}