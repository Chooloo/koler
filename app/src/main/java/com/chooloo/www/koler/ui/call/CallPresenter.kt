package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.telecom.Call.Details.*
import android.view.KeyEvent
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.call.Call
import com.chooloo.www.koler.data.call.Call.State.*
import com.chooloo.www.koler.data.call.CantHoldCallException
import com.chooloo.www.koler.data.call.CantMergeCallException
import com.chooloo.www.koler.data.call.CantSwapCallException
import com.chooloo.www.koler.service.CallService
import com.chooloo.www.koler.ui.base.BasePresenter

class CallPresenter<V : CallContract.View>(view: V) :
    BasePresenter<V>(view),
    CallContract.Presenter<V> {

    private var _currentCallId: String? = null
    private lateinit var _callTimeHandler: Handler
    private lateinit var _callTimeRunnable: Runnable

    override fun onStart() {
        _callTimeRunnable = Runnable {
            _callTimeHandler.postDelayed(_callTimeRunnable, 1000)
            displayCallTime()
        }
        _callTimeHandler = Handler(Looper.getMainLooper())
        _callTimeHandler.postDelayed(_callTimeRunnable, 1000)

        CallService.sIsActivityActive = true

        boundComponent.apply {
            proximityInteractor.acquire()
            screenInteractor.disableKeyboard()
            screenInteractor.setShowWhenLocked()
            callsInteractor.registerListener(this@CallPresenter)
            callsInteractor.mainCall?.let {
                onCallChanged(it)
                onMainCallChanged(it)
            }
        }
    }

    override fun onStop() {
        boundComponent.proximityInteractor.release()
    }


    override fun onAnswerClick() {
        _currentCallId?.let { boundComponent.callsInteractor.answerCall(it) }
    }

    override fun onRejectClick() {
        _currentCallId?.let { boundComponent.callsInteractor.rejectCall(it) }
    }

    override fun onSwapClick() {
        try {
            _currentCallId?.let { boundComponent.callsInteractor.swapCall(it) }
        } catch (e: CantSwapCallException) {
            view.showError(R.string.error_cant_swap_calls)
            e.printStackTrace()
        }
    }

    override fun onHoldClick() {
        try {
            _currentCallId?.let { boundComponent.callsInteractor.toggleHold(it) }
        } catch (e: CantHoldCallException) {
            view.showError(R.string.error_cant_hold_call)
            e.printStackTrace()
        }
    }

    override fun onMuteClick() {
        !view.isMuteActivated.also {
            boundComponent.audioInteractor.isMuted = it
            view.isMuteActivated = it
        }
    }

    override fun onMergeClick() {
        try {
            _currentCallId?.let { boundComponent.callsInteractor.mergeCall(it) }
        } catch (e: CantMergeCallException) {
            view.showError(R.string.error_cant_merge_call)
            e.printStackTrace()
        }
    }

    override fun onKeypadClick() {
        view.showDialpad()
    }

    override fun onAddCallClick() {
        view.showAddCallDialog()
    }

    override fun onSpeakerClick() {
        !view.isSpeakerActivated.also {
            boundComponent.audioInteractor.isSpeakerOn = it
            view.isSpeakerActivated = it
        }
    }

    override fun onKeypadKey(keyCode: Int, event: KeyEvent) {
        _currentCallId?.let { boundComponent.callsInteractor.invokeCallKey(it, event.number) }
    }


    override fun onNoCalls() {
        CallService.sIsActivityActive = false
        _callTimeHandler.removeCallbacksAndMessages(null)
        view.finish()
    }

    override fun onCallChanged(call: Call) {
        if (boundComponent.callsInteractor.getFirstState(HOLDING)?.id == _currentCallId) {
            view.hideHoldingBanner()
        } else if (call.isHolding && _currentCallId != call.id && !call.isInConference) {
            val account = boundComponent.phoneAccountsInteractor.lookupAccount(call.number)
            view.showHoldingBanner(
                """${account.displayString}${
                    boundComponent.stringInteractor.getString(R.string.warning_is_on_hold)
                }"""
            )
        } else if (boundComponent.callsInteractor.getStateCount(HOLDING) == 0) {
            view.hideHoldingBanner()
        }
    }

    override fun onMainCallChanged(call: Call) {
        _currentCallId = call.id
        displayCall(call)
    }


    private fun displayCallTime() {
        view.setElapsedTime(boundComponent.callsInteractor.mainCall?.durationTimeMilis)
    }

    private fun displayCall(call: Call) {
        if (call.isIncoming) {
            view.showIncomingCallUI()
        }

        if (call.isConference) {
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        val account = boundComponent.phoneAccountsInteractor.lookupAccount(call.number)
        account.photoUri?.let { view.imageURI = Uri.parse(it) }

        view.isHoldActivated = call.isHolding
        view.nameText = account.displayString
        view.isHoldEnabled = call.isCapable(CAPABILITY_HOLD)
        view.isMuteEnabled = call.isCapable(CAPABILITY_MUTE)
        view.isSwapEnabled = call.isCapable(CAPABILITY_SWAP_CONFERENCE)
        view.stateText = boundComponent.stringInteractor.getString(call.state.stringRes)

        if (call.isIncoming) {
            view.showIncomingCallUI()
        } else if (boundComponent.callsInteractor.isMultiCall) {
            view.showMultiActiveCallUI()
        } else {
            view.showActiveCallUI()
        }

        when (call.state) {
            INCOMING, ACTIVE -> view.stateTextColor =
                boundComponent.colorInteractor.getColor(R.color.green_foreground)
            HOLDING, DISCONNECTING, DISCONNECTED -> view.stateTextColor =
                boundComponent.colorInteractor.getColor(R.color.red_foreground)
        }
    }
}