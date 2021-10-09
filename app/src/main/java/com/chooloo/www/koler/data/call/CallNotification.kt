package com.chooloo.www.koler.data.call

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
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.call.Call.State.DISCONNECTED
import com.chooloo.www.koler.data.call.Call.State.DISCONNECTING
import com.chooloo.www.koler.interactor.audio.AudioInteractor
import com.chooloo.www.koler.interactor.calls.CallsInteractor
import com.chooloo.www.koler.receiver.CallBroadcastReceiver
import com.chooloo.www.koler.receiver.CallBroadcastReceiver.Companion.ACTION_HANGUP
import com.chooloo.www.koler.receiver.CallBroadcastReceiver.Companion.ACTION_MUTE
import com.chooloo.www.koler.receiver.CallBroadcastReceiver.Companion.ACTION_SPEAKER
import com.chooloo.www.koler.receiver.CallBroadcastReceiver.Companion.ACTION_UNMUTE
import com.chooloo.www.koler.receiver.CallBroadcastReceiver.Companion.ACTION_UNSPEAKER
import com.chooloo.www.koler.ui.call.CallActivity
import com.chooloo.www.koler.util.SingletonHolder

@RequiresApi(Build.VERSION_CODES.O)
class CallNotification(
    private val context: Context
) : CallsInteractor.Listener, AudioInteractor.Listener {
    private var _call: Call? = null
    private val componentRoot by lazy { (context.applicationContext as KolerApp).componentRoot }

    
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

    override fun onSpeakerChanged(isSpeaker: Boolean) {
        _call?.let { show(it) }
    }


    fun attach() {
        createNotificationChannel()
        componentRoot.callsInteractor.registerListener(this)
        componentRoot.audioInteractor.registerListener(this)
    }

    fun detach() {
        componentRoot.callsInteractor.unregisterListener(this)
        componentRoot.audioInteractor.unregisterListener(this)
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
            componentRoot.stringInteractor.getString(R.string.action_answer),
            _getCallPendingIntent(ACTION_ANSWER, 0)
        )
    }

    private val _hangupAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_call_end_black_24dp,
            componentRoot.stringInteractor.getString(R.string.action_hangup),
            _getCallPendingIntent(ACTION_HANGUP, 1)
        )
    }

    private val _muteAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_mic_black_24dp,
            componentRoot.stringInteractor.getString(R.string.call_action_mute),
            _getCallPendingIntent(ACTION_MUTE, 2)
        )
    }

    private val _unmuteAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_mic_off_black_24dp,
            componentRoot.stringInteractor.getString(R.string.call_action_unmute),
            _getCallPendingIntent(ACTION_UNMUTE, 3)
        )
    }

    private val _speakerAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_volume_down_black_24dp,
            componentRoot.stringInteractor.getString(R.string.call_action_speaker),
            _getCallPendingIntent(ACTION_SPEAKER, 4)
        )
    }

    private val _unspeakerAction by lazy {
        NotificationCompat.Action(
            R.drawable.ic_volume_up_black_24dp,
            componentRoot.stringInteractor.getString(R.string.call_action_speaker_off),
            _getCallPendingIntent(ACTION_UNSPEAKER, 5)
        )
    }

    private val _channel by lazy {
        NotificationChannelCompat.Builder(CHANNEL_ID, IMPORTANCE_HIGH)
            .setName(componentRoot.stringInteractor.getString(R.string.call_notification_channel_name))
            .setDescription(componentRoot.stringInteractor.getString(R.string.call_notification_channel_description))
            .setLightsEnabled(true)
            .build()
    }


    private fun _getCallIntent(callAction: String) =
        Intent(context, CallBroadcastReceiver::class.java).apply {
            action = callAction
            putExtra(EXTRA_NOTIFICATION_ID, ID)
        }

    private fun _getCallPendingIntent(callAction: String, rc: Int) =
        PendingIntent.getBroadcast(context, rc, _getCallIntent(callAction), FLAG_CANCEL_CURRENT)


    private fun buildNotification(call: Call, callback: (Notification) -> Unit) {
        componentRoot.phoneAccountsInteractor.lookupAccount(call.number) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setWhen(0)
                .setOngoing(true)
                .setColorized(true)
                .setPriority(PRIORITY)
                .setOnlyAlertOnce(true)
                .setContentTitle(it.displayString)
                .setSmallIcon(R.drawable.icon_full_144)
                .setContentIntent(_contentPendingIntent)
                .setColor(componentRoot.colorInteractor.getAttrColor(R.attr.colorSecondary))
                .setContentText(componentRoot.stringInteractor.getString(call.state.stringRes))
            if (call.isIncoming) {
                builder.addAction(_answerAction)
            }
            if (call.state !in arrayOf(DISCONNECTED, DISCONNECTING)) {
                builder.addAction(_hangupAction)
            }
            if (call.isCapable(CAPABILITY_MUTE)) {
                builder.addAction(if (componentRoot.audioInteractor.isMuted) _unmuteAction else _muteAction)
            }
            builder.addAction(if (componentRoot.audioInteractor.isSpeakerOn) _unspeakerAction else _speakerAction)
            callback.invoke(builder.build())
        }
    }


    fun show(call: Call) {
        buildNotification(call) {
            componentRoot.notificationManager.notify(ID, it)
        }
    }

    fun cancel() {
        componentRoot.notificationManager.cancel(ID)
    }

    fun createNotificationChannel() {
        componentRoot.notificationManager.createNotificationChannel(_channel)
    }


    companion object : SingletonHolder<CallNotification, Context>(::CallNotification) {
        const val ID = 420
        const val CHANNEL_ID = "call_notification_channel"
        const val PRIORITY = NotificationCompat.PRIORITY_LOW
    }
}