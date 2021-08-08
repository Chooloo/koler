package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.os.Handler
import com.chooloo.www.koler.App
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.CallDetails
import com.chooloo.www.koler.data.CallDetails.CallState.*
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.call.CallManager

class CallPresenter<V : CallContract.View>(mvpView: V) :
    BasePresenter<V>(mvpView),
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

        mvpView.callerNameText = phoneAccount.name ?: phoneAccount.number ?: "Unknown"
        phoneAccount.photoUri?.let { mvpView.callerImageURI = Uri.parse(it) }

        mvpView.stateText = App.resources?.getString(
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
            CONNECTING, DIALING -> mvpView.transitionToActiveUI()
            HOLDING -> mvpView.apply {
                getColor(R.color.red_foreground).let { mvpView.stateTextColor = it }
                animateStateTextAttention()
            }
            ACTIVE -> mvpView.apply {
                getColor(R.color.green_foreground).let { mvpView.stateTextColor = it }
                animateStateTextAttention()
                startStopwatch()
                transitionToActiveUI()
            }
            DISCONNECTED -> mvpView.apply {
                getColor(R.color.red_foreground).let { stateTextColor = it }
                animateStateTextAttention()
                stopStopwatch()
                Handler().postDelayed({ finish() }, 2000)
            }
        }
    }
}