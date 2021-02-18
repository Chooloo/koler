package com.chooloo.www.koler.ui.call

import android.view.KeyEvent
import com.chooloo.www.koler.ui.base.MvpPresenter

interface CallMvpPresenter<V : CallMvpView> : MvpPresenter<V> {

    // incoming call actions
    fun onAnswerClick()
    fun onRejectClick()

    // in call actions
    fun onAddCallClick()
    fun onKeypadClick()
    fun onMuteClick(isActivated: Boolean)
    fun onSpeakerClick(isActivated: Boolean)
    fun onHoldClick(isActivated: Boolean)
    fun onDialpadKeyClick(keyCode: Int, event: KeyEvent)
    
    fun onDetailsChanged()
    fun onStateChanged()
}