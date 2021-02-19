package com.chooloo.www.koler.ui.callactions

import com.chooloo.www.koler.ui.base.BasePresenter

class CallActionsPresenter<V : CallActionsMvpView> : BasePresenter<V>(), CallActionsMvpPresenter<V> {
    override fun onKeypadClick() {
        mvpView?.openKeypad()
    }

    override fun onAddCallClick() {
        mvpView?.addCall()
    }

    override fun onHoldClick(isActivated: Boolean) {
        mvpView?.toggleHold(!isActivated)
    }

    override fun onMuteClick(isActivated: Boolean) {
        mvpView?.toggleMute(!isActivated)
    }

    override fun onRecordClick(isActivated: Boolean) {
        mvpView?.startRecording()
    }

    override fun onSpeakerClick(isActivated: Boolean) {
        mvpView?.stopRecording()
    }
}