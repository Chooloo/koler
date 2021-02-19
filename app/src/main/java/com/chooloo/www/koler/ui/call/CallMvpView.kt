package com.chooloo.www.koler.ui.call

import android.telecom.Call
import com.chooloo.www.koler.ui.base.MvpView

interface CallMvpView : MvpView {
    fun answer()
    fun reject()
    fun updateDetails(details: Call.Details)
    fun updateState(state: Int)
    fun setAudioInCall()
    fun openCallActions()
}