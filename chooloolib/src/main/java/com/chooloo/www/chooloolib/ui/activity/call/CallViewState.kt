package com.chooloo.www.chooloolib.ui.activity.call
//
//import android.net.Uri
//import android.os.Build
//import android.telecom.PhoneAccountHandle
//import android.telecom.PhoneAccountSuggestion
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.chooloo.www.chooloolib.R
//import com.chooloo.www.chooloolib.domain.model.CallData
//import com.chooloo.www.chooloolib.domain.model.CallData.State.ACTIVE
//import com.chooloo.www.chooloolib.domain.model.CallData.State.DISCONNECTED
//import com.chooloo.www.chooloolib.domain.model.CallData.State.DISCONNECTING
//import com.chooloo.www.chooloolib.domain.model.CallData.State.HOLDING
//import com.chooloo.www.chooloolib.domain.model.CallData.State.INCOMING
//import com.chooloo.www.chooloolib.domain.model.CallData.State.SELECT_PHONE_ACCOUNT
//import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository
//import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository.AudioMode.NORMAL
//import com.chooloo.www.chooloolib.usecase.callaudio.ICallAudioUseCase
//import com.chooloo.www.chooloolib.domain.repository.color.ColorRepository
//import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
//import com.chooloo.www.chooloolib.domain.repository.proximity.ProximityRepository
//import com.chooloo.www.chooloolib.usecase.string.StringsInteractor
//import com.chooloo.www.chooloolib.utils.DataLiveEvent
//import com.chooloo.www.chooloolib.utils.LiveEvent
//import com.chooloo.www.chooloolib.utils.MutableDataLiveEvent
//import com.chooloo.www.chooloolib.utils.MutableLiveEvent
//import dagger.hilt.android.lifecycle.HiltViewModel
//import io.reactivex.disposables.CompositeDisposable
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class CallViewState @Inject constructor(
//    private val calls: ICallUseCase1,
//    private val audios: AudioRepository,
//    private val colors: ColorRepository,
//    private val phones: PhoneRepository,
//    private val strings: StringsInteractor,
//    private val disposables: CompositeDisposable,
//    private val callAudios: ICallAudioUseCase,
//    private val proximities: ProximityRepository,
//) :
//    BaseViewState(),
//    ICallUseCase1.Listener,
//    ICallAudioUseCase.Listener,
//    CallActions.CallActionsListener {
//
//    private val _name = MutableLiveData<String?>()
//    private val _imageRes = MutableLiveData<Int>()
//    private val _uiState = MutableLiveData<UIState>(UIState.ACTIVE)
//    private val _imageURI = MutableLiveData<Uri?>(null)
//    private val _bannerText = MutableLiveData<String?>()
//    private val _stateText = MutableLiveData<String?>()
//    private val _elapsedTime = MutableLiveData<Long?>()
//    private val _stateTextColor = MutableLiveData<Int>()
//
//    private val _isHoldEnabled = MutableLiveData<Boolean>()
//    private val _isMuteEnabled = MutableLiveData<Boolean>()
//    private val _isSwapEnabled = MutableLiveData<Boolean>()
//    private val _isMergeEnabled = MutableLiveData<Boolean>()
//    private val _isMuteActivated = MutableLiveData<Boolean>()
//    private val _isHoldActivated = MutableLiveData<Boolean>()
//    private val _isManageEnabled = MutableLiveData(false)
//    private val _isSpeakerEnabled = MutableLiveData(true)
//    private val _isSpeakerActivated = MutableLiveData<Boolean>()
//    private val _isBluetoothActivated = MutableLiveData<Boolean>()
//
//    private val _showDialerEvent = MutableLiveEvent()
//    private val _showDialpadEvent = MutableLiveEvent()
//    private val _askForRouteEvent = MutableLiveEvent()
//    private val _showCallManagerEvent = MutableLiveEvent()
//    private val _selectPhoneHandleEvent = MutableDataLiveEvent<List<PhoneAccountHandle>>()
//    private val _selectPhoneSuggestionEvent = MutableDataLiveEvent<List<PhoneAccountSuggestion>>()
//
//
//    val name = _name as LiveData<String?>
//    val imageRes = _imageRes as LiveData<Int>
//    val uiState = _uiState as LiveData<UIState>
//    val imageURI = _imageURI as LiveData<Uri?>
//    val bannerText = _bannerText as LiveData<String?>
//    val stateText = _stateText as LiveData<String?>
//    val elapsedTime = _elapsedTime as LiveData<Long?>
//    val stateTextColor = _stateTextColor as LiveData<Int>
//
//    val isHoldEnabled = _isHoldEnabled as LiveData<Boolean>
//    val isMuteEnabled = _isMuteEnabled as LiveData<Boolean>
//    val isSwapEnabled = _isSwapEnabled as LiveData<Boolean>
//    val isMergeEnabled = _isMergeEnabled as LiveData<Boolean>
//    val isMuteActivated = _isMuteActivated as LiveData<Boolean>
//    val isHoldActivated = _isHoldActivated as LiveData<Boolean>
//    val isManageEnabled = _isManageEnabled as LiveData<Boolean>
//    val isSpeakerEnabled = _isSpeakerEnabled as LiveData<Boolean>
//    val isSpeakerActivated = _isSpeakerActivated as LiveData<Boolean>
//    val isBluetoothActivated = _isBluetoothActivated as LiveData<Boolean>
//
//    val showDialerEvent = _showDialerEvent as LiveEvent
//    val showDialpadEvent = _showDialpadEvent as LiveEvent
//    val askForRouteEvent = _askForRouteEvent as LiveEvent
//    val showCallManagerEvent = _showCallManagerEvent as LiveEvent
//    val selectPhoneHandleEvent = _selectPhoneHandleEvent as DataLiveEvent<List<PhoneAccountHandle>>
//    val selectPhoneSuggestionEvent =
//        _selectPhoneSuggestionEvent as DataLiveEvent<List<PhoneAccountSuggestion>>
//
//    private var _currentCallId: String? = null
//
//    override fun onNoCalls() {
//        audios.audioMode = NORMAL
//        onFinish()
//    }
//
//    override fun onCallChanged(callData: CallData) {
//        if (calls.getFirstState(HOLDING)?.id == _currentCallId) {
//            _bannerText.value = null
//        } else if (callData.isHolding && _currentCallId != callData.id && !callData.isInConference) {
//            viewModelScope.launch {
//                _bannerText.value = String.format(
//                    strings.getString(R.string.explain_is_on_hold),
//                    phones.lookupAccount(callData.number)?.displayString ?: callData.number
//                )
//            }
//        } else if (calls.getStateCount(HOLDING) == 0) {
//            _bannerText.value = null
//        }
//    }
//
//    override fun onMainCallChanged(callData: CallData) {
//        _currentCallId = callData.id
//
//        if (callData.isEnterprise) {
//            _imageRes.value = R.drawable.corporate_fare
//        }
//
//        if (callData.isIncoming) {
//            _uiState.value = UIState.INCOMING
//        }
//
//        _stateText.value = strings.getString(callData.state.stringRes)
//
//        when {
//            callData.isIncoming -> _uiState.value = UIState.INCOMING
//            calls.isMultiCall -> _uiState.value = UIState.MULTI
//            else -> _uiState.value = UIState.ACTIVE
//        }
//
//        when (callData.state) {
//            ACTIVE,
//            INCOMING -> _stateTextColor.value =
//                colors.getColor(R.color.on_positive)
//
//            HOLDING,
//            DISCONNECTING,
//            DISCONNECTED -> _stateTextColor.value =
//                colors.getColor(R.color.on_negative)
//
//            else -> {}
//        }
//
//        if (callData.state == SELECT_PHONE_ACCOUNT && !callData.phoneAccountSelected) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                _selectPhoneSuggestionEvent.call(callData.suggestedPhoneAccounts)
//            } else {
//                _selectPhoneHandleEvent.call(callData.availablePhoneAccounts)
//            }
//        }
//    }
//    enum class UIState {
//        MULTI,
//        ACTIVE,
//        INCOMING
//    }
//}