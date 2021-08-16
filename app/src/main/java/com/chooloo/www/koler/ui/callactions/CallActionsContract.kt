package com.chooloo.www.koler.ui.callactions

import android.view.KeyEvent
import com.chooloo.www.koler.ui.base.BaseContract

interface CallActionsContract : BaseContract {
    interface View : BaseContract.View {
        fun addCall()
        fun openDialpad()
        fun stopRecording()
        fun startRecording()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onHoldClick()
        fun onMuteClick()
        fun onRecordClick()
        fun onKeypadClick()
        fun onAddCallClick()
        fun onSpeakerClick()
        fun onKeypadKey(keyCode: Int, event: KeyEvent)
    }
}