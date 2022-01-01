package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.telecom.Call.Details.*
import android.view.KeyEvent
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.call.Call
import com.chooloo.www.koler.data.call.Call.State.*
import com.chooloo.www.koler.data.call.CantHoldCallException
import com.chooloo.www.koler.data.call.CantMergeCallException
import com.chooloo.www.koler.data.call.CantSwapCallException
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractor.AudioRoute
import com.chooloo.www.koler.service.CallService
import com.chooloo.www.koler.ui.base.BaseController
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CallController<V : CallContract.View>(view: V) :
    BaseController<V>(view),
    CallContract.Controller<V> {

    private var _currentCallId: String? = null
    private var _timerDisposable: Disposable? = null

    override fun onStart() {
        _timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { displayCallTime() }

        CallService.sIsActivityActive = true

        component.apply {
            proximities.acquire()
            screens.disableKeyboard()
            screens.setShowWhenLocked()
            calls.registerListener(this@CallController)
            callAudios.registerListener(this@CallController)
            calls.mainCall?.let {
                onCallChanged(it)
                onMainCallChanged(it)
                callAudios.isMuted?.let(this@CallController::onMuteChanged)
                callAudios.audioRoute?.let(this@CallController::onAudioRouteChanged)
            }
        }
    }

    override fun onStop() {
        component.proximities.release()
        _timerDisposable?.dispose()
        CallService.sIsActivityActive = false
    }


    override fun onAnswerClick() {
        _currentCallId?.let { component.calls.answerCall(it) }
    }

    override fun onRejectClick() {
        _currentCallId?.let { component.calls.rejectCall(it) }
    }

    override fun onSwapClick() {
        try {
            _currentCallId?.let { component.calls.swapCall(it) }
        } catch (e: CantSwapCallException) {
            view.showError(R.string.error_cant_swap_calls)
            e.printStackTrace()
        }
    }

    override fun onHoldClick() {
        try {
            _currentCallId?.let { component.calls.toggleHold(it) }
        } catch (e: CantHoldCallException) {
            view.showError(R.string.error_cant_hold_call)
            e.printStackTrace()
        }
    }

    override fun onMuteClick() {
        component.callAudios.isMuted = !view.isMuteActivated
    }

    override fun onMergeClick() {
        try {
            _currentCallId?.let { component.calls.mergeCall(it) }
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
        component.callAudios.apply {
            if (supportedAudioRoutes.contains(AudioRoute.BLUETOOTH)) {
                component.dialogs.askForRoute { audioRoute = it }
            } else {
                isSpeakerOn = !view.isSpeakerActivated
            }
        }
    }

    override fun onKeypadKey(keyCode: Int, event: KeyEvent) {
        _currentCallId?.let { component.calls.invokeCallKey(it, event.number) }
    }


    override fun onNoCalls() {
        view.finish()
    }

    override fun onCallChanged(call: Call) {
        if (component.calls.getFirstState(HOLDING)?.id == _currentCallId) {
            view.hideHoldingBanner()
        } else if (call.isHolding && _currentCallId != call.id && !call.isInConference) {
            component.phones.lookupAccount(call.number) {
                view.showHoldingBanner(
                    String.format(
                        component.strings.getString(R.string.explain_is_on_hold),
                        it?.displayString ?: call.number
                    )
                )
            }
        } else if (component.calls.getStateCount(HOLDING) == 0) {
            view.hideHoldingBanner()
        }
    }

    override fun onMainCallChanged(call: Call) {
        _currentCallId = call.id
        displayCall(call)
    }


    override fun onMuteChanged(isMuted: Boolean) {
        view.isMuteActivated = isMuted
    }

    override fun onAudioRouteChanged(audioRoute: AudioRoute) {
        view.isSpeakerActivated = audioRoute == AudioRoute.SPEAKER
        view.isBluetoothActivated = audioRoute == AudioRoute.BLUETOOTH
    }

    private fun displayCallTime() {
        component.calls.mainCall?.let {
            if (it.isStarted) {
                view.setElapsedTime(it.durationTimeMilis)
            } else {
                view.setElapsedTime(null)
            }
        }
    }

    private fun displayCall(call: Call) {
        if (call.isIncoming) {
            view.showIncomingCallUI()
        }

        if (call.isConference) {
            view.nameText = component.strings.getString(R.string.conference)
        } else {
            component.phones.lookupAccount(call.number) { account ->
                account?.photoUri?.let { view.imageURI = Uri.parse(it) }
                view.nameText = account?.displayString ?: call.number
            }
        }

        view.isHoldActivated = call.isHolding
        view.isHoldEnabled = call.isCapable(CAPABILITY_HOLD)
        view.isMuteEnabled = call.isCapable(CAPABILITY_MUTE)
        view.isSwapEnabled = call.isCapable(CAPABILITY_SWAP_CONFERENCE)
        view.stateText = component.strings.getString(call.state.stringRes)

        when {
            call.isIncoming -> view.showIncomingCallUI()
            component.calls.isMultiCall -> view.showMultiActiveCallUI()
            else -> view.showActiveCallUI()
        }

        when (call.state) {
            INCOMING, ACTIVE -> view.stateTextColor =
                component.colors.getColor(R.color.green_foreground)
            HOLDING, DISCONNECTING, DISCONNECTED -> view.stateTextColor =
                component.colors.getColor(R.color.red_foreground)
        }
    }

    override fun onAudioRouteSelected(audioRoute: AudioRoute) {

    }
}