package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.telecom.Call
import android.telecom.Call.*
import com.chooloo.www.koler.App
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.util.call.CallManager

class CallPresenter<V : CallMvpView> : BasePresenter<V>(), CallMvpPresenter<V> {
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

    override fun onDetailsChanged(details: Call.Details?) {
        mvpView?.getContact(details?.handle.toString())?.let {
            mvpView?.callerNameText = it.name
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
            STATE_ACTIVE -> mvpView?.switchToActiveCallUI()
            STATE_DISCONNECTED -> mvpView?.finish()
        }
    }

    inner class CallCallback : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            super.onStateChanged(call, state)
            onStateChanged(state)
        }

        override fun onDetailsChanged(call: Call, details: Call.Details) {
            super.onDetailsChanged(call, details)
            onDetailsChanged(details)
        }
    }
}