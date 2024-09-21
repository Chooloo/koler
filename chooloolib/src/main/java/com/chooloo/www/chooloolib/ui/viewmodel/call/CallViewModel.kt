package com.chooloo.www.chooloolib.ui.viewmodel.call

import android.telecom.PhoneAccountHandle
import com.chooloo.www.chooloolib.domain.model.AudioRoute
import com.chooloo.www.chooloolib.domain.model.CallActionType
import com.chooloo.www.chooloolib.domain.model.CallerData
import com.chooloo.www.chooloolib.domain.model.ICallData
import com.chooloo.www.chooloolib.domain.repository.call.CallRepository.CallsState
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.StateFlow


interface CallViewModel : BaseViewModel {
    companion object {
        data class UiState(
            val call: ICallData? = null,
            val audioRoute: AudioRoute? = null,
            val isMuteActive: Boolean = false,
            val isManageOpen: Boolean = false,
            val isKeypadOpen: Boolean = false,
            val isDialpadOpen: Boolean = false,
            val isSelectAudioOpen: Boolean = false,
            val isSelectAccountOpen: Boolean = false,
            val calls: List<ICallData> = listOf(),
            val callerData: CallerData = CallerData(),
            val callsState: CallsState = CallsState.UNKNOWN,
            val visibleActions: List<CallActionType> = listOf(),
            val pendingAccountHandle: PhoneAccountHandle? = null,
            val supportedAudioRoutes: List<AudioRoute> = listOf()
        )
    }

    val uiState: StateFlow<UiState>

    fun onDismissManage()
    fun onDismissKeypad()
    fun onDismissDialpad()
    fun onDismissSelectAudioRouter()
    fun onDismissSelectPhoneAccount()

    fun onKeypadClick(key: Char)
    fun onCallActionClick(action: CallActionType)

    fun onSelectAudioRoute(audioRoute: AudioRoute)
    fun onSelectPhoneAccount(phoneAccount: PhoneAccountHandle)

    sealed class Errors {
        class CantJoinCallException : Errors()
        class CantHoldCallException : Errors()
        class CantSwapCallException : Errors()
        class CantMergeCallException : Errors()
    }
}