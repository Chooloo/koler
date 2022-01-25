package com.chooloo.www.chooloolib.ui.call

import android.net.Uri
import android.telecom.Call.Details.*
import android.view.KeyEvent
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.chooloolib.data.call.Call.State.*
import com.chooloo.www.chooloolib.data.call.CantHoldCallException
import com.chooloo.www.chooloolib.data.call.CantMergeCallException
import com.chooloo.www.chooloolib.data.call.CantSwapCallException
import com.chooloo.www.chooloolib.interactor.audio.AudioInteractor
import com.chooloo.www.chooloolib.interactor.audio.AudioInteractor.AudioMode.*
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudioInteractor
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudioInteractor.AudioRoute
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptInteractor
import com.chooloo.www.chooloolib.interactor.proximity.ProximityInteractor
import com.chooloo.www.chooloolib.interactor.screen.ScreenInteractor
import com.chooloo.www.chooloolib.interactor.string.StringInteractor
import com.chooloo.www.chooloolib.service.CallService
import com.chooloo.www.chooloolib.ui.base.BaseController
import com.chooloo.www.chooloolib.ui.callitems.CallItemsFragment
import com.chooloo.www.chooloolib.ui.dialer.DialerFragment
import com.chooloo.www.chooloolib.ui.dialpad.DialpadFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CallController<V : CallContract.View> @Inject constructor(
    view: V,
    private val callsInteractor: CallsInteractor,
    private val audioInteractor: AudioInteractor,
    private val colorInteractor: ColorInteractor,
    private val phonesInteractor: PhonesInteractor,
    private val promptInteractor: PromptInteractor,
    private val stringInteractor: StringInteractor,
    private val screenInteractor: ScreenInteractor,
    private val dialogsInteractor: DialogsInteractor,
    private val callAudioInteractor: CallAudioInteractor,
    private val proximityInteractor: ProximityInteractor,
) :
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

        proximityInteractor.acquire()
        screenInteractor.disableKeyboard()
        screenInteractor.setShowWhenLocked()
        callsInteractor.registerListener(this@CallController)
        callAudioInteractor.registerListener(this@CallController)
        callsInteractor.mainCall?.let {
            onCallChanged(it)
            onMainCallChanged(it)
            callAudioInteractor.isMuted?.let(this@CallController::onMuteChanged)
            callAudioInteractor.audioRoute?.let(this@CallController::onAudioRouteChanged)
        }

        view.isManageEnabled = false
    }

    override fun onStop() {
        proximityInteractor.release()
        _timerDisposable?.dispose()
        CallService.sIsActivityActive = false
    }


    override fun onAnswerClick() {
        _currentCallId?.let { callsInteractor.answerCall(it) }
    }

    override fun onRejectClick() {
        _currentCallId?.let { callsInteractor.rejectCall(it) }
    }

    override fun onSwapClick() {
        try {
            _currentCallId?.let { callsInteractor.swapCall(it) }
        } catch (e: CantSwapCallException) {
            view.showError(R.string.error_cant_swap_calls)
            e.printStackTrace()
        }
    }

    override fun onHoldClick() {
        try {
            _currentCallId?.let { callsInteractor.toggleHold(it) }
        } catch (e: CantHoldCallException) {
            view.showError(R.string.error_cant_hold_call)
            e.printStackTrace()
        }
    }

    override fun onMuteClick() {
        callAudioInteractor.isMuted = !view.isMuteActivated
    }

    override fun onMergeClick() {
        try {
            _currentCallId?.let { callsInteractor.mergeCall(it) }
        } catch (e: CantMergeCallException) {
            view.showError(R.string.error_cant_merge_call)
            e.printStackTrace()
        }
    }

    override fun onKeypadClick() {
        promptInteractor.showFragment(DialpadFragment.newInstance().apply {
            setOnKeyDownListener(::onKeypadKey)
        })
    }

    override fun onAddCallClick() {
        promptInteractor.showFragment(DialerFragment.newInstance())
    }

    override fun onSpeakerClick() {
        callAudioInteractor.apply {
            if (supportedAudioRoutes.contains(AudioRoute.BLUETOOTH)) {
                dialogsInteractor.askForRoute { audioRoute = it }
            } else {
                isSpeakerOn = !view.isSpeakerActivated
            }
        }
    }

    override fun onKeypadKey(keyCode: Int, event: KeyEvent) {
        _currentCallId?.let { callsInteractor.invokeCallKey(it, event.number) }
    }


    override fun onNoCalls() {
        audioInteractor.audioMode = NORMAL
        view.finish()
    }

    override fun onCallChanged(call: Call) {
        if (callsInteractor.getFirstState(HOLDING)?.id == _currentCallId) {
            view.hideHoldingBanner()
        } else if (call.isHolding && _currentCallId != call.id && !call.isInConference) {
            phonesInteractor.lookupAccount(call.number) {
                view.showHoldingBanner(
                    String.format(
                        stringInteractor.getString(R.string.explain_is_on_hold),
                        it?.displayString ?: call.number
                    )
                )
            }
        } else if (callsInteractor.getStateCount(HOLDING) == 0) {
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
        callsInteractor.mainCall?.let {
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
            view.nameText = stringInteractor.getString(R.string.conference)
        } else {
            phonesInteractor.lookupAccount(call.number) { account ->
                account?.photoUri?.let { view.imageURI = Uri.parse(it) }
                view.nameText = account?.displayString ?: call.number
            }
        }

        view.isHoldActivated = call.isHolding
        view.isManageEnabled = call.isConference
        view.isHoldEnabled = call.isCapable(CAPABILITY_HOLD)
        view.isMuteEnabled = call.isCapable(CAPABILITY_MUTE)
        view.isSwapEnabled = call.isCapable(CAPABILITY_SWAP_CONFERENCE)
        view.stateText = stringInteractor.getString(call.state.stringRes)

        when {
            call.isIncoming -> view.showIncomingCallUI()
            callsInteractor.isMultiCall -> view.showMultiActiveCallUI()
            else -> view.showActiveCallUI()
        }

        when (call.state) {
            INCOMING, ACTIVE -> view.stateTextColor =
                colorInteractor.getColor(R.color.green_foreground)
            HOLDING, DISCONNECTING, DISCONNECTED -> view.stateTextColor =
                colorInteractor.getColor(R.color.red_foreground)
        }
    }

    override fun onManageClick() {
        callsInteractor.mainCall?.children?.let {
            promptInteractor.showFragment(
                CallItemsFragment.newInstance().apply { controller.calls = it }
            )
        }
    }
}