package com.chooloo.www.koler.ui.callactions

import com.chooloo.www.koler.ui.base.MvpView

interface CallActionsMvpView : MvpView {
    fun addCall()
    fun openDialpad()
    fun startRecording()
    fun stopRecording()
    fun toggleSpeaker(isSpeaker: Boolean)
    fun toggleMute(isMute: Boolean)
}