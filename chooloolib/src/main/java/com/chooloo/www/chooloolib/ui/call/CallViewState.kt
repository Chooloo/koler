package com.chooloo.www.chooloolib.ui.call

import android.net.Uri
import android.telecom.Call.Details.*
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor.AudioMode.NORMAL
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor.AudioRoute
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.proximity.ProximitiesInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.model.Call
import com.chooloo.www.chooloolib.model.Call.State.*
import com.chooloo.www.chooloolib.model.CantHoldCallException
import com.chooloo.www.chooloolib.model.CantMergeCallException
import com.chooloo.www.chooloolib.model.CantSwapCallException
import com.chooloo.www.chooloolib.service.CallService
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.ui.widgets.CallActions
import com.chooloo.www.chooloolib.util.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CallViewState @Inject constructor(
    private val disposables: CompositeDisposable,
    private val callsInteractor: CallsInteractor,
    private val audiosInteractor: AudiosInteractor,
    private val colorsInteractor: ColorsInteractor,
    private val phonesInteractor: PhonesInteractor,
    private val stringsInteractor: StringsInteractor,
    private val callAudiosInteractor: CallAudiosInteractor,
    private val proximitiesInteractor: ProximitiesInteractor,
) :
    BaseViewState(),
    CallsInteractor.Listener,
    CallAudiosInteractor.Listener,
    CallActions.CallActionsListener {

    val name = MutableLiveData<String?>()
    val uiState = MutableLiveData<UIState>()
    val imageURI = MutableLiveData<Uri?>(null)
    val bannerText = MutableLiveData<String>()
    val stateText = MutableLiveData<String?>()
    val elapsedTime = MutableLiveData(0L)
    val stateTextColor = MutableLiveData<Int>()

    val isHoldEnabled = MutableLiveData(false)
    val isMuteEnabled = MutableLiveData(false)
    val isSwapEnabled = MutableLiveData(false)
    val isMergeEnabled = MutableLiveData(false)
    val isManageEnabled = MutableLiveData(false)
    val isSpeakerEnabled = MutableLiveData(true)
    val isMuteActivated = MutableLiveData(false)
    val isHoldActivated = MutableLiveData(false)
    val isSpeakerActivated = MutableLiveData(false)
    val isBluetoothActivated = MutableLiveData(false)

    val showDialerEvent = LiveEvent()
    val showDialpadEvent = LiveEvent()
    val askForRouteEvent = LiveEvent()
    val showCallManagerEvent = LiveEvent()

    var _currentCallId: String? = null


    override fun attach() {
        disposables.add(Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { displayCallTime() })

        CallService.sIsActivityActive = true

        proximitiesInteractor.acquire()
        callsInteractor.registerListener(this@CallViewState)
        callAudiosInteractor.registerListener(this@CallViewState)
        callsInteractor.mainCall?.let {
            onCallChanged(it)
            onMainCallChanged(it)
            callAudiosInteractor.isMuted?.let(this@CallViewState::onMuteChanged)
            callAudiosInteractor.audioRoute?.let(this@CallViewState::onAudioRouteChanged)
        }

        isManageEnabled.value = false
    }

    override fun detach() {
        proximitiesInteractor.release()
        CallService.sIsActivityActive = false
    }

    fun onAnswerClick() {
        _currentCallId?.let { callsInteractor.answerCall(it) }
    }

    fun onRejectClick() {
        _currentCallId?.let { callsInteractor.rejectCall(it) }
    }

    override fun onSwapClick() {
        try {
            _currentCallId?.let { callsInteractor.swapCall(it) }
        } catch (e: CantSwapCallException) {
            errorEvent.call(R.string.error_cant_swap_calls)
            e.printStackTrace()
        }
    }

    override fun onHoldClick() {
        try {
            _currentCallId?.let { callsInteractor.toggleHold(it) }
        } catch (e: CantHoldCallException) {
            errorEvent.call(R.string.error_cant_hold_call)
            e.printStackTrace()
        }
    }

    override fun onMuteClick() {
        callAudiosInteractor.isMuted = !isMuteActivated.value!!
    }

    override fun onMergeClick() {
        try {
            _currentCallId?.let { callsInteractor.mergeCall(it) }
        } catch (e: CantMergeCallException) {
            errorEvent.call(R.string.error_cant_merge_call)
            e.printStackTrace()
        }
    }

    override fun onKeypadClick() {
        showDialpadEvent.call()
    }

    override fun onAddCallClick() {
        showDialerEvent.call()
    }

    override fun onSpeakerClick() {
        callAudiosInteractor.apply {
            if (supportedAudioRoutes.contains(AudioRoute.BLUETOOTH)) {
                askForRouteEvent.call()
            } else {
                isSpeakerOn = !isSpeakerActivated.value!!
            }
        }
    }

    fun onCharKey(char: Char) {
        _currentCallId?.let { callsInteractor.invokeCallKey(it, char) }
    }


    override fun onNoCalls() {
        audiosInteractor.audioMode = NORMAL
        finishEvent.call()
    }

    override fun onCallChanged(call: Call) {
        if (callsInteractor.getFirstState(HOLDING)?.id == _currentCallId) {
            bannerText.value = null
        } else if (call.isHolding && _currentCallId != call.id && !call.isInConference) {
            phonesInteractor.lookupAccount(call.number) {
                bannerText.value = String.format(
                    stringsInteractor.getString(R.string.explain_is_on_hold),
                    it?.displayString ?: call.number
                )
            }
        } else if (callsInteractor.getStateCount(HOLDING) == 0) {
            bannerText.value = null
        }
    }

    override fun onMainCallChanged(call: Call) {
        _currentCallId = call.id
        displayCall(call)
    }


    override fun onMuteChanged(isMuted: Boolean) {
        isMuteActivated.value = isMuted
    }

    override fun onAudioRouteChanged(audioRoute: AudioRoute) {
        isSpeakerActivated.value = audioRoute == AudioRoute.SPEAKER
        isBluetoothActivated.value = audioRoute == AudioRoute.BLUETOOTH
    }

    private fun displayCallTime() {
        callsInteractor.mainCall?.let {
            elapsedTime.value = if (it.isStarted) it.durationTimeMilis else null
        }
    }

    private fun displayCall(call: Call) {
        if (call.isIncoming) {
            uiState.value = UIState.INCOMING
        }

        if (call.isConference) {
            name.value = stringsInteractor.getString(R.string.conference)
        } else {
            phonesInteractor.lookupAccount(call.number) { account ->
                account?.photoUri?.let { imageURI.value = Uri.parse(it) }
                name.value = account?.displayString ?: call.number
            }
        }

        isHoldActivated.value = call.isHolding
        isManageEnabled.value = call.isConference
        isHoldEnabled.value = call.isCapable(CAPABILITY_HOLD)
        isMuteEnabled.value = call.isCapable(CAPABILITY_MUTE)
        isSwapEnabled.value = call.isCapable(CAPABILITY_SWAP_CONFERENCE)
        stateText.value = stringsInteractor.getString(call.state.stringRes)

        when {
            call.isIncoming -> uiState.value = UIState.INCOMING
            callsInteractor.isMultiCall -> uiState.value = UIState.MULTI
            else -> uiState.value = UIState.ACTIVE
        }

        when (call.state) {
            INCOMING, ACTIVE -> stateTextColor.value =
                colorsInteractor.getColor(R.color.green_foreground)
            HOLDING, DISCONNECTING, DISCONNECTED -> stateTextColor.value =
                colorsInteractor.getColor(R.color.red_foreground)
        }
    }

    fun onManageClick() {
        showCallManagerEvent.call()
    }

    fun onAudioRoutePicked(audioRoute: AudioRoute) {
        callAudiosInteractor.audioRoute = audioRoute
    }

    enum class UIState {
        MULTI,
        ACTIVE,
        INCOMING
    }
}