package com.chooloo.www.chooloolib.ui.viewmodel.dialpad

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.clipboard.ClipboardRepository
import com.chooloo.www.chooloolib.domain.repository.contact.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.recent.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.telecom.TelecomRepository
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModelImpl
import com.chooloo.www.chooloolib.ui.viewmodel.dialpad.DialpadViewModel.Companion.UiState
import com.chooloo.www.chooloolib.utils.parseDialpadText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialpadViewModelImpl @Inject constructor(
    private val audioRepository: AudioRepository,
    private val recentRepository: RecentRepository,
    private val telecomRepository: TelecomRepository,
    private val contactRepository: ContactRepository,
    private val clipboardRepository: ClipboardRepository,
    private val preferenceRepository: PreferenceRepository,
) : BaseViewModelImpl(), DialpadViewModel {
    private val _uiState = MutableStateFlow(UiState())

    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private fun setValue(changer: (prev: String) -> String) {
        _uiState.update { it.copy(value = changer(it.value)) }
    }

    override fun onCallClick() {
        setValue {
            viewModelScope.launch {
                if (it.isNotEmpty()) {
                    telecomRepository.callNumber(it)
                } else {
                    telecomRepository.callNumber(it)
                    val lastOutgoingCall = recentRepository.getLastOutgoingCall()
                    setValue { lastOutgoingCall }
                }
            }
            it
        }
    }

    override fun onTextPasted() {
        setValue { (clipboardRepository.lastCopiedText ?: "").parseDialpadText() }
    }

    override fun onDeleteClick() {
        setValue { it.dropLast(1) }
    }

    override fun onDeleteLongClick() {
        setValue { "" }
    }

    override fun onAddContactClick() {
        viewModelScope.launch {
            contactRepository.createContact(uiState.value.value)
        }
    }

    override fun onKeyClick(key: String) {
        viewModelScope.launch {
            if (preferenceRepository.isDialpadTonesEnabled.value) audioRepository.playToneByChar(key[0])
            if (preferenceRepository.isDialpadVibrateEnabled.value) audioRepository.vibrate(
                AudioRepository.SHORT_VIBRATE_LENGTH
            )
        }
        setValue {
            val newValue = it + key
            viewModelScope.launch {
                telecomRepository.handleSpecialChars(newValue)
            }
            if (newValue.isNotEmpty()) {
                viewModelScope.launch {
                    val suggestions = contactRepository.getContacts(filter = newValue)
                        .map(ContactData.Companion::fromRecord)
                    _uiState.update { state -> state.copy(suggestions = suggestions) }
                }
            }
            newValue
        }
    }

    override fun onKeyLongClick(key: String) {
        when (key) {
            "0" -> setValue { "$key+" }
            "1" -> viewModelScope.launch { telecomRepository.callVoicemail() }
        }
    }
}