package com.chooloo.www.chooloolib.ui.call

import android.net.Uri
import android.os.Build
import android.telecom.Call.Details.*
import android.telecom.PhoneAccountHandle
import android.telecom.PhoneAccountSuggestion
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.api.service.CallService
import com.chooloo.www.chooloolib.data.model.Call
import com.chooloo.www.chooloolib.data.model.Call.State.*
import com.chooloo.www.chooloolib.data.model.CantHoldCallException
import com.chooloo.www.chooloolib.data.model.CantMergeCallException
import com.chooloo.www.chooloolib.data.model.CantSwapCallException
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor.AudioMode.NORMAL
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor.AudioRoute
import com.chooloo.www.chooloolib.interactor.calls.CallsInteractor
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.proximity.ProximitiesInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.ui.widgets.CallActions
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.chooloolib.util.MutableDataLiveEvent
import com.chooloo.www.chooloolib.util.MutableLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class CallViewState @Inject constructor(
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

    private val _name = MutableLiveData<String?>()
    private val _imageRes = MutableLiveData<Int>()
    private val _uiState = MutableLiveData<UIState>()
    private val _imageURI = MutableLiveData<Uri?>(null)
    private val _bannerText = MutableLiveData<String?>()
    private val _stateText = MutableLiveData<String?>()
    private val _elapsedTime = MutableLiveData<Long?>()
    private val _stateTextColor = MutableLiveData<Int>()

    private val _isHoldEnabled = MutableLiveData<Boolean>()
    private val _isMuteEnabled = MutableLiveData<Boolean>()
    private val _isSwapEnabled = MutableLiveData<Boolean>()
    private val _isMergeEnabled = MutableLiveData<Boolean>()
    private val _isMuteActivated = MutableLiveData<Boolean>()
    private val _isHoldActivated = MutableLiveData<Boolean>()
    private val _isManageEnabled = MutableLiveData(false)
    private val _isSpeakerEnabled = MutableLiveData(true)
    private val _isSpeakerActivated = MutableLiveData<Boolean>()
    private val _isBluetoothActivated = MutableLiveData<Boolean>()

    private val _showDialerEvent = MutableLiveEvent()
    private val _showDialpadEvent = MutableLiveEvent()
    private val _askForRouteEvent = MutableLiveEvent()
    private val _showCallManagerEvent = MutableLiveEvent()
    private val _selectPhoneHandleEvent = MutableDataLiveEvent<List<PhoneAccountHandle>>()
    private val _selectPhoneSuggestionEvent = MutableDataLiveEvent<List<PhoneAccountSuggestion>>()


    val name = _name as LiveData<String?>
    val imageRes = _imageRes as LiveData<Int>
    val uiState = _uiState as LiveData<UIState>
    val imageURI = _imageURI as LiveData<Uri?>
    val bannerText = _bannerText as LiveData<String?>
    val stateText = _stateText as LiveData<String?>
    val elapsedTime = _elapsedTime as LiveData<Long?>
    val stateTextColor = _stateTextColor as LiveData<Int>

    val isHoldEnabled = _isHoldEnabled as LiveData<Boolean>
    val isMuteEnabled = _isMuteEnabled as LiveData<Boolean>
    val isSwapEnabled = _isSwapEnabled as LiveData<Boolean>
    val isMergeEnabled = _isMergeEnabled as LiveData<Boolean>
    val isMuteActivated = _isMuteActivated as LiveData<Boolean>
    val isHoldActivated = _isHoldActivated as LiveData<Boolean>
    val isManageEnabled = _isManageEnabled as LiveData<Boolean>
    val isSpeakerEnabled = _isSpeakerEnabled as LiveData<Boolean>
    val isSpeakerActivated = _isSpeakerActivated as LiveData<Boolean>
    val isBluetoothActivated = _isBluetoothActivated as LiveData<Boolean>

    val showDialerEvent = _showDialerEvent as LiveEvent
    val showDialpadEvent = _showDialpadEvent as LiveEvent
    val askForRouteEvent = _askForRouteEvent as LiveEvent
    val showCallManagerEvent = _showCallManagerEvent as LiveEvent
    val selectPhoneHandleEvent = _selectPhoneHandleEvent as DataLiveEvent<List<PhoneAccountHandle>>
    val selectPhoneSuggestionEvent =
        _selectPhoneSuggestionEvent as DataLiveEvent<List<PhoneAccountSuggestion>>

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

        _isManageEnabled.value = false
    }

    override fun detach() {
        proximities.release()
        CallService.sIsActivityActive = false
    }

    fun onAnswerClick() {
        _currentCallId?.let(calls::answerCall)
    }

    fun onRejectClick() {
        _currentCallId?.let(calls::rejectCall)
    }

    override fun onSwapClick() {
        try {
            _currentCallId?.let(calls::swapCall)
        } catch (e: CantSwapCallException) {
            onError(R.string.error_cant_swap_calls)
            e.printStackTrace()
        }
    }

    override fun onHoldClick() {
        try {
            _currentCallId?.let(calls::toggleHold)
        } catch (e: CantHoldCallException) {
            onError(R.string.error_cant_hold_call)
            e.printStackTrace()
        }
    }

    override fun onMuteClick() {
        callAudios.isMuted = !isMuteActivated.value!!
    }

    override fun onMergeClick() {
        try {
            _currentCallId?.let(calls::mergeCall)
        } catch (e: CantMergeCallException) {
            onError(R.string.error_cant_merge_call)
            e.printStackTrace()
        }
    }

    override fun onKeypadClick() {
        _showDialpadEvent.call()
    }

    override fun onAddCallClick() {
        _showDialerEvent.call()
    }

    override fun onSpeakerClick() {
        callAudios.apply {
            if (supportedAudioRoutes.contains(AudioRoute.BLUETOOTH)) {
                _askForRouteEvent.call()
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
        onFinish()
    }

    override fun onCallChanged(call: Call) {
        if (calls.getFirstState(HOLDING)?.id == _currentCallId) {
            _bannerText.value = null
        } else if (call.isHolding && _currentCallId != call.id && !call.isInConference) {
            viewModelScope.launch {
                _bannerText.value = String.format(
                    strings.getString(R.string.explain_is_on_hold),
                    phones.lookupAccount(call.number)?.displayString ?: call.number
                )
            }
        } else if (calls.getStateCount(HOLDING) == 0) {
            _bannerText.value = null
        }
    }

    override fun onMainCallChanged(call: Call) {
        _currentCallId = call.id

        if (call.isEnterprise) {
            _imageRes.value = R.drawable.corporate_fare
        }

        if (call.isIncoming) {
            _uiState.value = UIState.INCOMING
        }

        if (call.isConference) {
            _name.value = strings.getString(R.string.conference)
        } else {
            viewModelScope.launch {
                val account = phones.lookupAccount(call.number)
                account?.photoUri?.let { _imageURI.value = Uri.parse(it) }
                _name.value = account?.displayString ?: call.number
            }
        }

        _isHoldActivated.value = call.isHolding
        _isManageEnabled.value = call.isConference
        _isHoldEnabled.value = call.isCapable(CAPABILITY_HOLD)
        _isMuteEnabled.value = call.isCapable(CAPABILITY_MUTE)
        _isSwapEnabled.value = call.isCapable(CAPABILITY_SWAP_CONFERENCE)
        _stateText.value = strings.getString(call.state.stringRes)

        when {
            call.isIncoming -> _uiState.value = UIState.INCOMING
            calls.isMultiCall -> _uiState.value = UIState.MULTI
            else -> _uiState.value = UIState.ACTIVE
        }

        when (call.state) {
            ACTIVE,
            INCOMING -> _stateTextColor.value =
                colors.getColor(R.color.positive_background)

            HOLDING,
            DISCONNECTING,
            DISCONNECTED -> _stateTextColor.value =
                colors.getAttrColor(R.attr.colorError)

            else -> {}
        }

        if (call.state == SELECT_PHONE_ACCOUNT && !call.phoneAccountSelected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                _selectPhoneSuggestionEvent.call(call.suggestedPhoneAccounts)
            } else {
                _selectPhoneHandleEvent.call(call.availablePhoneAccounts)
            }
        }
    }


    override fun onMuteChanged(isMuted: Boolean) {
        _isMuteActivated.value = isMuted
    }

    override fun onAudioRouteChanged(audioRoute: AudioRoute) {
        _isSpeakerActivated.value = audioRoute == AudioRoute.SPEAKER
        _isBluetoothActivated.value = audioRoute == AudioRoute.BLUETOOTH
    }

    private fun displayCallTime() {
        calls.mainCall?.let {
            _elapsedTime.value = if (it.isStarted) it.durationTimeMilis else null
        }
    }

    fun onManageClick() {
        _showCallManagerEvent.call()
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