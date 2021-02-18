package com.chooloo.www.koler.ui.call

import android.telecom.Call
import com.chooloo.www.koler.ui.base.MvpView

interface CallMvpView : MvpView {

    // call actions
    fun answer()
    fun reject()
    fun addCall()
    fun toggleHold(isHold: Boolean)
    fun pressDialpadKey(keyChar: Char)

    // in call actions
    fun toggleMute(isMute: Boolean)
    fun toggleSpeaker(isSpeaker: Boolean)
    fun showDialpad()
    
    fun updateDetails(details: Call.Details)
    fun updateState(state: Int)

    fun setAudioInCall()
}