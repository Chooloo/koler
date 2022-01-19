package com.chooloo.www.chooloolib.data.call

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Build
import android.telecom.Call.Details.CAPABILITY_MUTE
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import com.chooloo.www.chooloolib.BaseApp
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.call.Call.State.DISCONNECTED
import com.chooloo.www.chooloolib.data.call.Call.State.DISCONNECTING
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudioInteractor
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver.Companion.ACTION_HANGUP
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver.Companion.ACTION_MUTE
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver.Companion.ACTION_SPEAKER
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver.Companion.ACTION_UNMUTE
import com.chooloo.www.chooloolib.receiver.CallBroadcastReceiver.Companion.ACTION_UNSPEAKER
import com.chooloo.www.chooloolib.ui.call.CallActivity
import com.chooloo.www.chooloolib.util.SingletonHolder

@RequiresApi(Build.VERSION_CODES.O)
class CallNotification(
    private val context: Context
) : CallsInteractor.Listener, CallAudioInteractor.Listener {
    private var _call: Call? = null
    private val component by lazy { (context.applicationContext as BaseApp).component }


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

    override fun onAudioRouteChanged(audioRoute: CallAudioInteractor.AudioRoute) {
        _call?.let { show(it) }
    }


    fun attach() {
        createNotificationChannel()
        component.calls.registerListener(this)
        component.callAudios.registerListener(this)
    }

    fun detach() {
        component.calls.unregisterListener(this)
        component.callAudios.unregisterListener(this)
        cancel()
    }

    private val _contentPendingIntent by lazy {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, CallActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            },
            0
        )
    }

    private val _answerAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_call_black_24dp,
            component.strings.getString(R.string.action_answer),
            getCallPendingIntent(ACTION_ANSWER, 0)
        )
    }

    private val _hangupAction by lazy {
        NotificationCompat.Action(
            R.drawable.round_call_end_24,
            component.strings.getString(R.string.action_hangup),
            getCallPendingIntent(ACTION_HANGUP, 1)
        )
    }

    private val _muteAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_mic_black_24dp,
            component.strings.getString(R.string.call_action_mute),
            getCallPendingIntent(ACTION_MUTE, 2)
        )
    }

    private val _unmuteAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_mic_off_black_24dp,
            component.strings.getString(R.string.call_action_unmute),
            getCallPendingIntent(ACTION_UNMUTE, 3)
        )
    }

    private val _speakerAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_volume_down_black_24dp,
            component.strings.getString(R.string.call_action_speaker),
            getCallPendingIntent(ACTION_SPEAKER, 4)
        )
    }

    private val _unspeakerAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_volume_up_black_24dp,
            component.strings.getString(R.string.call_action_speaker_off),
            getCallPendingIntent(ACTION_UNSPEAKER, 5)
        )
    }

    private val _channel by lazy {
        NotificationChannelCompat.Builder(CHANNEL_ID, IMPORTANCE_HIGH)
            .setName(component.strings.getString(R.string.call_notification_channel_name))
            .setDescription(component.strings.getString(R.string.call_notification_channel_description))
            .setLightsEnabled(true)
            .build()
    }


    private fun getCallIntent(callAction: String) =
        Intent(context, CallBroadcastReceiver::class.java).apply {
            action = callAction
            putExtra(EXTRA_NOTIFICATION_ID, ID)
        }

    private fun getCallPendingIntent(callAction: String, rc: Int) =
        PendingIntent.getBroadcast(context, rc, getCallIntent(callAction), FLAG_CANCEL_CURRENT)


    private fun buildNotification(call: Call, callback: (Notification) -> Unit) {
        component.phones.lookupAccount(call.number) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setWhen(0)
                .setOngoing(true)
                .setColorized(true)
                .setPriority(PRIORITY)
                .setOnlyAlertOnce(true)
                .setContentTitle(it?.displayString ?: call.number)
                .setSmallIcon(R.drawable.icon_full_144)
                .setContentIntent(_contentPendingIntent)
                .setColor(component.colors.getAttrColor(R.attr.colorSecondary))
                .setContentText(component.strings.getString(call.state.stringRes))
            if (call.isIncoming) {
                builder.addAction(_answerAction)
            }
            if (call.state !in arrayOf(DISCONNECTED, DISCONNECTING)) {
                builder.addAction(_hangupAction)
            }
            component.callAudios.isMuted?.let { isMuted ->
                if (call.isCapable(CAPABILITY_MUTE)) {
                    builder.addAction(if (isMuted) _unmuteAction else _muteAction)
                }
            }
            component.callAudios.isSpeakerOn?.let { isSpeakerOn ->
                builder.addAction(if (isSpeakerOn) _unspeakerAction else _speakerAction)
            }
            callback.invoke(builder.build())
        }
    }


    fun show(call: Call) {
        buildNotification(call) {
            component.notificationManager.notify(ID, it)
        }
    }

    fun cancel() {
        component.notificationManager.cancel(ID)
    }

    fun createNotificationChannel() {
        component.notificationManager.createNotificationChannel(_channel)
    }


    companion object : SingletonHolder<CallNotification, Context>(::CallNotification) {
        const val ID = 420
        const val CHANNEL_ID = "call_notification_channel"
        const val PRIORITY = NotificationCompat.PRIORITY_LOW
    }
}