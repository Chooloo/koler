package com.chooloo.www.chooloolib.ui.call

import android.net.Uri
import android.os.Build
import android.telecom.Call.Details.*
import android.telecom.PhoneAccountHandle
import android.telecom.PhoneAccountSuggestion
import android.telecom.TelecomManager
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
import com.chooloo.www.chooloolib.util.DataLiveEvent
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
    private val telecomManger: TelecomManager,
    private val calls: CallsInteractor,
    private val audios: AudiosInteractor,
    private val colors: ColorsInteractor,
    private val phones: PhonesInteractor,
    private val strings: StringsInteractor,
    private val disposables: CompositeDisposable,
    private val callAudios: CallAudiosInteractor,
    private val proximities: ProximitiesInteractor,
) :
    BaseViewState(),
    CallsInteractor.Listener,
    CallAudiosInteractor.Listener,
    CallActions.CallActionsListener {

    val name = MutableLiveData<String?>()
    val imageRes = MutableLiveData<Int>()
    val uiState = MutableLiveData<UIState>()
    val imageURI = MutableLiveData<Uri?>(null)
    val bannerText = MutableLiveData<String>()
    val stateText = MutableLiveData<String?>()
    val elapsedTime = MutableLiveData<Long>()
    val stateTextColor = MutableLiveData<Int>()

    val isHoldEnabled = MutableLiveData<Boolean>()
    val isMuteEnabled = MutableLiveData<Boolean>()
    val isSwapEnabled = MutableLiveData<Boolean>()
    val isMergeEnabled = MutableLiveData<Boolean>()
    val isMuteActivated = MutableLiveData<Boolean>()
    val isHoldActivated = MutableLiveData<Boolean>()
    val isManageEnabled = MutableLiveData(false)
    val isSpeakerEnabled = MutableLiveData(true)
    val isSpeakerActivated = MutableLiveData<Boolean>()
    val isBluetoothActivated = MutableLiveData<Boolean>()

    val showDialerEvent = LiveEvent()
    val showDialpadEvent = LiveEvent()
    val askForRouteEvent = LiveEvent()
    val showCallManagerEvent = LiveEvent()
    val selectPhoneHandleEvent = DataLiveEvent<List<PhoneAccountHandle>>()
    val selectPhoneSuggestionEvent = DataLiveEvent<List<PhoneAccountSuggestion>>()

    private var _currentCallId: String? = null


    override fun attach() {
        disposables.add(Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { displayCallTime() })

        CallService.sIsActivityActive = true

        proximities.acquire()
        calls.registerListener(this@CallViewState)
        callAudios.registerListener(this@CallViewState)
        calls.mainCall?.let {
            onCallChanged(it)
            onMainCallChanged(it)
            callAudios.isMuted?.let(this@CallViewState::onMuteChanged)
            callAudios.audioRoute?.let(this@CallViewState::onAudioRouteChanged)
        }


        isManageEnabled.value = false
    }

    override fun detach() {
        proximities.release()
        CallService.sIsActivityActive = false
    }

    fun onAnswerClick() {
        _currentCallId?.let { calls.answerCall(it) }
    }

    fun onRejectClick() {
        _currentCallId?.let { calls.rejectCall(it) }
    }

    override fun onSwapClick() {
        try {
            _currentCallId?.let { calls.swapCall(it) }
        } catch (e: CantSwapCallException) {
            errorEvent.call(R.string.error_cant_swap_calls)
            e.printStackTrace()
        }
    }

    override fun onHoldClick() {
        try {
            _currentCallId?.let { calls.toggleHold(it) }
        } catch (e: CantHoldCallException) {
            errorEvent.call(R.string.error_cant_hold_call)
            e.printStackTrace()
        }
    }

    override fun onMuteClick() {
        callAudios.isMuted = !isMuteActivated.value!!
    }

    override fun onMergeClick() {
        try {
            _currentCallId?.let { calls.mergeCall(it) }
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
        callAudios.apply {
            if (supportedAudioRoutes.contains(AudioRoute.BLUETOOTH)) {
                askForRouteEvent.call()
            } else {
                isSpeakerOn = !isSpeakerActivated.value!!
            }
        }
    }

    fun onCharKey(char: Char) {
        _currentCallId?.let { calls.invokeCallKey(it, char) }
    }


    override fun onNoCalls() {
        audios.audioMode = NORMAL
        finishEvent.call()
    }

    override fun onCallChanged(call: Call) {
        if (calls.getFirstState(HOLDING)?.id == _currentCallId) {
            bannerText.value = null
        } else if (call.isHolding && _currentCallId != call.id && !call.isInConference) {
            phones.lookupAccount(call.number) {
                bannerText.value = String.format(
                    strings.getString(R.string.explain_is_on_hold),
                    it?.displayString ?: call.number
                )
            }
        } else if (calls.getStateCount(HOLDING) == 0) {
            bannerText.value = null
        }
    }

    override fun onMainCallChanged(call: Call) {
        _currentCallId = call.id

        if (call.isEnterprise) {
            imageRes.value = R.drawable.round_business_24
        }

        if (call.isIncoming) {
            uiState.value = UIState.INCOMING
        }

        if (call.isConference) {
            name.value = strings.getString(R.string.conference)
        } else {
            phones.lookupAccount(call.number) { account ->
                account?.photoUri?.let { imageURI.value = Uri.parse(it) }
                name.value = account?.displayString ?: call.number
            }
        }

        isHoldActivated.value = call.isHolding
        isManageEnabled.value = call.isConference
        isHoldEnabled.value = call.isCapable(CAPABILITY_HOLD)
        isMuteEnabled.value = call.isCapable(CAPABILITY_MUTE)
        isSwapEnabled.value = call.isCapable(CAPABILITY_SWAP_CONFERENCE)
        stateText.value = strings.getString(call.state.stringRes)

        when {
            call.isIncoming -> uiState.value = UIState.INCOMING
            calls.isMultiCall -> uiState.value = UIState.MULTI
            else -> uiState.value = UIState.ACTIVE
        }

        when (call.state) {
            INCOMING, ACTIVE -> stateTextColor.value =
                colors.getColor(R.color.green_foreground)
            HOLDING, DISCONNECTING, DISCONNECTED -> stateTextColor.value =
                colors.getColor(R.color.red_foreground)
        }

        if (call.state == SELECT_PHONE_ACCOUNT && !call.phoneAccountSelected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                selectPhoneSuggestionEvent.call(call.suggestedPhoneAccounts)
            } else {
                selectPhoneHandleEvent.call(call.availablePhoneAccounts)
            }
        }
    }


    override fun onMuteChanged(isMuted: Boolean) {
        isMuteActivated.value = isMuted
    }

    override fun onAudioRouteChanged(audioRoute: AudioRoute) {
        isSpeakerActivated.value = audioRoute == AudioRoute.SPEAKER
        isBluetoothActivated.value = audioRoute == AudioRoute.BLUETOOTH
    }

    private fun displayCallTime() {
        calls.mainCall?.let {
            elapsedTime.value = if (it.isStarted) it.durationTimeMilis else null
        }
    }

    fun onManageClick() {
        showCallManagerEvent.call()
    }

    fun onAudioRoutePicked(audioRoute: AudioRoute) {
        callAudios.audioRoute = audioRoute
    }

    fun onPhoneAccountHandleSelected(phoneAccountHandle: PhoneAccountHandle) {
        calls.mainCall?.selectPhoneAccount(phoneAccountHandle)
    }

    enum class UIState {
        MULTI,
        ACTIVE,
        INCOMING
    }
}