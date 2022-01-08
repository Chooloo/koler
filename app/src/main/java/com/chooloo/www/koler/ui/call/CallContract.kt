package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.view.KeyEvent
import com.chooloo.www.koler.data.call.Call
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractor
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractor.AudioRoute
import com.chooloo.www.koler.interactor.calls.CallsInteractor
import com.chooloo.www.koler.ui.base.BaseContract
import com.chooloo.www.koler.ui.widgets.CallActions

interface CallContract : BaseContract {
    interface View : BaseContract.View {
        var imageURI: Uri?
        var nameText: String?
        var stateText: String?
        var stateTextColor: Int

        var isHoldEnabled: Boolean
        var isMuteEnabled: Boolean
        var isSwapEnabled: Boolean
        var isMergeEnabled: Boolean
        var isManageEnabled: Boolean
        var isSpeakerEnabled: Boolean

        var isMuteActivated: Boolean
        var isHoldActivated: Boolean
        var isSpeakerActivated: Boolean
        var isBluetoothActivated: Boolean


        fun showDialpad()
        fun showActiveCallUI()
        fun showAddCallDialog()
        fun showIncomingCallUI()
        fun showMultiActiveCallUI()
        fun setElapsedTime(duration: Long?)
        fun showCallsManager(calls: List<Call>)

        fun showHoldingBanner(number: String)
        fun hideHoldingBanner()
    }

    interface Controller<V : View> :
        BaseContract.Controller<V>,
        CallsInteractor.Listener,
        CallAudioInteractor.Listener,
        CallActions.CallActionsListener {

        fun onAnswerClick()
        fun onRejectClick()
        fun onManageClick()
        fun onKeypadKey(keyCode: Int, event: KeyEvent)
    }
}