package com.chooloo.www.chooloolib.ui.call

import android.net.Uri
import android.view.KeyEvent
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseContract
import com.chooloo.www.chooloolib.ui.widgets.CallActions

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


        fun showActiveCallUI()
        fun showIncomingCallUI()
        fun showMultiActiveCallUI()
        fun setElapsedTime(duration: Long?)

        fun showHoldingBanner(number: String)
        fun hideHoldingBanner()
    }

    interface Controller<V : View> :
        BaseContract.Controller<V>,
        CallsInteractor.Listener,
        CallAudiosInteractor.Listener,
        CallActions.CallActionsListener {

        fun onAnswerClick()
        fun onRejectClick()
        fun onManageClick()
        fun onKeypadKey(keyCode: Int, event: KeyEvent)
    }
}