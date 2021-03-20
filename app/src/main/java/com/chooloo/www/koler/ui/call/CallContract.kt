package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.telecom.Call
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.ui.base.BaseContract

interface CallContract : BaseContract {
    interface View : BaseContract.View {
        var stateText: String?
        var stateTextColor: Int
        var callerNameText: String?
        var callerImageURI: Uri?

        fun getContact(number: String?): Contact
        fun transitionToActiveUI()
        fun blinkStateText()
        fun startStopwatch()
        fun stopStopwatch()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onAnswerClick()
        fun onRejectClick()
        fun onInitialUI()
        fun onDetailsChanged(details: Call.Details?)
        fun onStateChanged(state: Int?)
    }
}