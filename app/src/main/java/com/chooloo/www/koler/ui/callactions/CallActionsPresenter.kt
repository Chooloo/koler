package com.chooloo.www.koler.ui.callactions

import android.view.KeyEvent
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.util.call.CallsManager

class CallActionsPresenter<V : CallActionsContract.View> :
    BasePresenter<V>(),
    CallActionsContract.Presenter<V> {

    private var _isMuted = false
    private var _isSpeaker = false
    private var _isRecording = false
    private var _isHolding = false

    override fun onHoldClick() {
        _isHolding = !_isHolding
        CallsManager.firstCall?.hold()
    }

    override fun onMuteClick() {
        _isMuted = !_isMuted
        mvpView?.toggleMute(_isMuted)
    }

    override fun onRecordClick() {
        if (_isRecording) {
            mvpView?.stopRecording()
            _isRecording = false
        } else {
            CallsManager.firstCall?.let {
                mvpView?.startRecording()
                mvpView?.showMessage("Recording...")
                _isRecording = true
            } ?: run {
                mvpView?.showError("No active calls to record")
            }
        }
    }

    override fun onKeypadClick() {
        mvpView?.openDialpad()
    }

    override fun onAddCallClick() {
        mvpView?.addCall()
    }

    override fun onSpeakerClick() {
        _isSpeaker = !_isSpeaker
        mvpView?.toggleSpeaker(_isSpeaker)
    }

    override fun onKeypadKey(keyCode: Int, event: KeyEvent) {
        CallsManager.firstCall?.invokeKeypadChar(event.number)
    }
}