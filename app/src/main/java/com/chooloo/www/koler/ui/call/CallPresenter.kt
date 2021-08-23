package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.os.Handler
import com.chooloo.www.koler.R
import com.chooloo.www.koler.call.CallManager
import com.chooloo.www.koler.data.CallDetails
import com.chooloo.www.koler.data.CallDetails.CallState.*
import com.chooloo.www.koler.ui.base.BasePresenter

class CallPresenter<V : CallContract.View>(view: V) :
    BasePresenter<V>(view),
    CallContract.Presenter<V> {

    override fun onAnswerClick() {
        CallManager.answer()
    }

    override fun onRejectClick() {
        CallManager.reject()
    }

    override fun onCallDetailsChanged(callDetails: CallDetails?) {
        if (callDetails == null) {
            return
        }

        val phoneAccount = callDetails.phoneAccount
        val callState = callDetails.callState

        view.callerNameText = phoneAccount.name ?: phoneAccount.number ?: "Unknown"
        phoneAccount.photoUri?.let { view.callerImageURI = Uri.parse(it) }

        view.stateText = boundComponent.stringInteractor.getString(
            when (callState) {
                ACTIVE -> R.string.call_status_active
                DISCONNECTED -> R.string.call_status_disconnected
                RINGING -> R.string.call_status_incoming
                DIALING -> R.string.call_status_dialing
                CONNECTING -> R.string.call_status_dialing
                HOLDING -> R.string.call_status_holding
                else -> R.string.call_status_active
            }
        )

        when (callState) {
            CONNECTING, DIALING -> view.transitionToActiveUI()
            HOLDING -> view.apply {
                boundComponent.colorInteractor.getColor(R.color.red_foreground)
                    .let { view.stateTextColor = it }
                animateStateTextAttention()
            }
            ACTIVE -> view.apply {
                boundComponent.colorInteractor.getColor(R.color.green_foreground)
                    .let { view.stateTextColor = it }
                animateStateTextAttention()
                startStopwatch()
                transitionToActiveUI()
            }
            DISCONNECTED -> view.apply {
                boundComponent.colorInteractor.getColor(R.color.red_foreground)
                    .let { stateTextColor = it }
                animateStateTextAttention()
                stopStopwatch()
                Handler().postDelayed({ finish() }, 2000)
            }
        }
    }
}