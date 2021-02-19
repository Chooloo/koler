package com.chooloo.www.koler.ui.callactions

import com.chooloo.www.koler.ui.base.MvpPresenter

interface CallActionsMvpPresenter<V : CallActionsMvpView> : MvpPresenter<V> {
    fun onKeypadClick()
    fun onAddCallClick()
    fun onHoldClick(isActivated: Boolean)
    fun onMuteClick(isActivated: Boolean)
    fun onRecordClick(isActivated: Boolean)
    fun onSpeakerClick(isActivated: Boolean)
}