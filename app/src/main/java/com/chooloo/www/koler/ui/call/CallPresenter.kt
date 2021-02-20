package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.telecom.Call
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter

class CallPresenter<V : CallMvpView> : BasePresenter<V>(), CallMvpPresenter<V> {
    override fun onAnswerClick() {
        mvpView?.answer()
    }

    override fun onRejectClick() {
        mvpView?.reject()
    }

    override fun onCallActionsClick() {
        mvpView?.openCallActions()
    }

    override fun onDetailsChanged(details: Call.Details) {
        val contact = mvpView?.lookupContact(details.handle.toString())
        mvpView?.apply {
            callerName = details.callerDisplayName
            contact?.photoUri?.let {
                callerImage = Uri.parse(it)
            }
        }
    }

    override fun onStateChanged(state: Int) {
        mvpView?.status = mvpView?.getString(when (state) {
            Call.STATE_ACTIVE -> R.string.status_call_active
            Call.STATE_DISCONNECTED -> R.string.status_call_disconnected
            Call.STATE_RINGING -> R.string.status_call_incoming
            Call.STATE_DIALING -> R.string.status_call_dialing
            Call.STATE_CONNECTING -> R.string.status_call_dialing
            Call.STATE_HOLDING -> R.string.status_call_holding
            else -> R.string.status_call_active
        })
    }
}