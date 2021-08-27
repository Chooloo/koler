package com.chooloo.www.koler.ui.callactions

import android.view.KeyEvent
import com.chooloo.www.koler.call.CallManager
import com.chooloo.www.koler.interactor.audio.AudioInteractor
import com.chooloo.www.koler.interactor.audio.AudioInteractor.AudioMode.*
import com.chooloo.www.koler.ui.base.BasePresenter

class CallActionsPresenter<V : CallActionsContract.View>(view: V) :
    BasePresenter<V>(view),
    CallActionsContract.Presenter<V> {

    private var _isMuted = false
    private var _isSpeaker = false
    private var _isRecording = false
    private var _isHolding = false

    override fun onStart() {
        boundComponent.audioInteractor.audioMode = IN_CALL
    }

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
            _isRecording = false
        } else {
            if (CallManager.sCall != null) {
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
    }

    override fun onSpeakerClick() {
        _isSpeaker = !_isSpeaker
        boundComponent.audioInteractor.isSpeakerOn = _isSpeaker
    }

    override fun onKeypadKey(keyCode: Int, event: KeyEvent) {
        CallManager.keypad(event.number)
    }
}