package com.chooloo.www.koler.ui.callactions

import android.view.KeyEvent
import com.chooloo.www.koler.ui.base.BaseContract

interface CallActionsContract : BaseContract {
    interface View : BaseContract.View {
        fun addCall()
        fun openDialpad()
        fun startRecording()
        fun stopRecording()
        fun toggleSpeaker(isSpeaker: Boolean)
        fun toggleMute(isMute: Boolean)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onKeypadClick()
        fun onAddCallClick()
        fun onHoldClick()
        fun onMuteClick()
        fun onRecordClick()
        fun onSpeakerClick()
        fun onKeypadKey(keyCode: Int, event: KeyEvent)
    }
}