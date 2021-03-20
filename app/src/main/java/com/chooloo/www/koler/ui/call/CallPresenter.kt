package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.os.Handler
import android.telecom.Call
import android.telecom.Call.*
import com.chooloo.www.koler.App
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.util.call.CallManager

class CallPresenter<V : CallContract.View> : BasePresenter<V>(), CallContract.Presenter<V> {
    private val _callCallback by lazy { CallCallback() }

    override fun attach(mvpView: V) {
        super.attach(mvpView)
        CallManager.registerCallback(_callCallback)
    }

    override fun detach() {
        super.detach()
        CallManager.unregisterCallback(_callCallback)
    }

    override fun onInitialUI() {
        onDetailsChanged(CallManager.sCall?.details)
        onStateChanged(CallManager.sCall?.state)
    }

    override fun onAnswerClick() {
        CallManager.answer()
    }

    override fun onRejectClick() {
        CallManager.reject()
    }

    override fun onDetailsChanged(details: Details?) {
        mvpView?.getContact(CallManager.getNumber(details))?.let {
            mvpView?.callerNameText = it.name ?: it.number
            it.photoUri?.let { mvpView?.callerImageURI = Uri.parse(it) }
        }
    }

    override fun onStateChanged(state: Int?) {
        mvpView?.stateText = App.resources?.getString(
            when (state) {
                STATE_ACTIVE -> R.string.status_call_active
                STATE_DISCONNECTED -> R.string.status_call_disconnected
                STATE_RINGING -> R.string.status_call_incoming
                STATE_DIALING -> R.string.status_call_dialing
                STATE_CONNECTING -> R.string.status_call_dialing
                STATE_HOLDING -> R.string.status_call_holding
                else -> R.string.status_call_active
            }
        )

        when (state) {
            STATE_ACTIVE -> {
                mvpView?.getColor(R.color.green_dark)?.let { mvpView?.stateTextColor = it }
                mvpView?.blinkStateText()
                mvpView?.startStopwatch()
                mvpView?.transitionToActiveUI()
            }
            STATE_DISCONNECTED -> {
                mvpView?.getColor(R.color.red_dark)?.let { mvpView?.stateTextColor = it }
                mvpView?.blinkStateText()
                mvpView?.stopStopwatch()
                Handler().postDelayed({ mvpView?.finish() }, 2000)
            }
            STATE_HOLDING -> {
                mvpView?.getColor(R.color.red_dark)?.let { mvpView?.stateTextColor = it }
                mvpView?.blinkStateText()
            }
            STATE_CONNECTING -> mvpView?.transitionToActiveUI()
        }
    }

    inner class CallCallback : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            super.onStateChanged(call, state)
            onStateChanged(state)
        }

        override fun onDetailsChanged(call: Call, details: Details) {
            super.onDetailsChanged(call, details)
            onDetailsChanged(details)
        }
    }
}