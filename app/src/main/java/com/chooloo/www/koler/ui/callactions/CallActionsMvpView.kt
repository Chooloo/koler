package com.chooloo.www.koler.ui.callactions

import com.chooloo.www.koler.ui.base.MvpView

interface CallActionsMvpView : MvpView {
    fun addCall()
    fun openKeypad()
    fun startRecording()
    fun stopRecording()
    fun toggleHold(isHold: Boolean)
    fun toggleSpeaker(isSpeaker: Boolean)
    fun toggleMute(isMute: Boolean)
    fun pressKeypadKey(keyChar: Char)
}