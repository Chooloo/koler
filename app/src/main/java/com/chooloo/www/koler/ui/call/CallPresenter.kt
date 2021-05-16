package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.os.Handler
import com.chooloo.www.koler.App
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.util.call.CallItem
import com.chooloo.www.koler.util.call.CallItem.Companion.CallState.*
import com.chooloo.www.koler.util.call.CallsManager

class CallPresenter<V : CallContract.View> : BasePresenter<V>(), CallContract.Presenter<V> {
    override fun attach(mvpView: V) {
        super.attach(mvpView)
        CallsManager.registerListener(object : CallsManager.CallsListener {
            override fun onCallChanged(callItem: CallItem) {
                super.onCallChanged(callItem)
                if (callItem.isTheSameCall(CallsManager.firstCall)) {
                    displayPrimaryCall(callItem)
                } else {
                    mvpView.updateSecondaryCall(callItem)
                }
            }
        })
    }

    override fun onAnswerClick() {
        CallsManager.firstCall?.answer()
    }

    override fun onRejectClick() {
        CallsManager.firstCall?.reject()
    }

    override fun onDisplayCalls() {
        CallsManager.firstCall?.let { displayPrimaryCall(it) }
        CallsManager.secondaryCalls.forEach { mvpView?.updateSecondaryCall(it) }
    }

    private fun displayPrimaryCall(callItem: CallItem) {
        val account = mvpView?.getCallAccount(callItem)
        mvpView?.apply {
            account?.photoUri?.let { callerImageURI = Uri.parse(it) }
            stateText = App.resources?.getString(callItem.state.stringRes)
            callerNameText = account?.name ?: account?.number ?: "Unknown"
        }

        when (callItem.state) {
            CONNECTING -> mvpView?.transitionToActiveUI()
            HOLDING -> mvpView?.apply {
                getColor(R.color.red_foreground).let { mvpView?.stateTextColor = it }
                blinkStateText()
            }
            ACTIVE -> mvpView?.apply {
                getColor(R.color.green_foreground).let { mvpView?.stateTextColor = it }
                blinkStateText()
                startStopwatch()
                transitionToActiveUI()
            }
            DISCONNECTED -> mvpView?.apply {
                getColor(R.color.red_foreground).let { stateTextColor = it }
                blinkStateText()
                stopStopwatch()
                if (CallsManager.sCalls.size == 0) {
                    Handler().postDelayed({ finish() }, 2000)
                }
            }
            else -> {
            }
        }
    }
}