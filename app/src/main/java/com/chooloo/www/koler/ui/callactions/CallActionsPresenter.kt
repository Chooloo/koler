package com.chooloo.www.koler.ui.callactions

import android.view.KeyEvent
import com.chooloo.www.koler.call.CallManager
import com.chooloo.www.koler.ui.base.BasePresenter

class CallActionsPresenter<V : CallActionsContract.View>(view: V) :
    BasePresenter<V>(view),
    CallActionsContract.Presenter<V> {

    private var _isMuted = false
    private var _isSpeaker = false
    private var _isRecording = false
    private var _isHolding = false

    override fun onHoldClick() {
        _isHolding = !_isHolding
        CallManager.hold(_isHolding)
    }

    override fun onMuteClick() {
        _isMuted = !_isMuted
        boundComponent.audioInteractor.isMuted = _isMuted
    }

    override fun onRecordClick() {
        if (_isRecording) {
            view.stopRecording()
            _isRecording = false
        } else {
            if (CallManager.sCall != null) {
                view.startRecording()
                view.showMessage("Recording...")
                _isRecording = true
            } else {
                view.showError("No active calls to record")
            }
        }
    }

    override fun onKeypadClick() {
        view.openDialpad()
    }

    override fun onAddCallClick() {
        view.addCall()
    }

    override fun onSpeakerClick() {
        _isSpeaker = !_isSpeaker
        boundComponent.audioInteractor.isSpeakerOn = _isSpeaker
    }

    override fun onKeypadKey(keyCode: Int, event: KeyEvent) {
        CallManager.keypad(event.number)
    }
}