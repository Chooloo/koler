package com.chooloo.www.koler.ui.call

import android.telecom.Call
import android.view.KeyEvent
import com.chooloo.www.koler.ui.base.BasePresenter

class CallPresenter<V : CallMvpView> : BasePresenter<V>(), CallMvpPresenter<V> {
    override fun onAnswerClick() {
        mvpView?.answer()
    }

    override fun onRejectClick() {
        mvpView?.reject()
    }

    override fun onAddCallClick() {
        // TODO this should get a call as a parameter or something idk do it
        mvpView?.addCall()
    }

    override fun onKeypadClick() {
        mvpView?.showDialpad()
    }

    override fun onMuteClick(isActivated: Boolean) {
        mvpView?.toggleHold(isActivated)
    }

    override fun onSpeakerClick(isActivated: Boolean) {
        mvpView?.toggleSpeaker(isActivated)
    }

    override fun onHoldClick(isActivated: Boolean) {
        mvpView?.toggleHold(isActivated)
    }

    override fun onDialpadKeyClick(keyCode: Int, event: KeyEvent) {
        mvpView?.pressDialpadKey(event.unicodeChar.toChar())
    }

    override fun onDetailsChanged(details: Call.Details) {
        mvpView?.updateDetails(details)
    }

    override fun onStateChanged(state: Int) {
        mvpView?.updateState(state)
    }
}