package com.chooloo.www.koler.ui.callactions

import android.view.KeyEvent
import com.chooloo.www.koler.ui.base.MvpPresenter

interface CallActionsMvpPresenter<V : CallActionsMvpView> : MvpPresenter<V> {
    fun onKeypadClick()
    fun onAddCallClick()
    fun onHoldClick()
    fun onMuteClick()
    fun onRecordClick()
    fun onSpeakerClick()
    fun onKeypadKey(keyCode: Int, event: KeyEvent)
}