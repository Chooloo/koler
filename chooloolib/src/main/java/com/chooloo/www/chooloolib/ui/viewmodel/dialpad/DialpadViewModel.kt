package com.chooloo.www.chooloolib.ui.viewmodel.dialpad

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.repository.audio.AudioRepository
import com.chooloo.www.chooloolib.domain.repository.clipboard.ClipboardRepository
import com.chooloo.www.chooloolib.domain.repository.contact.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.phone.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.recent.RecentRepository
import com.chooloo.www.chooloolib.domain.repository.telecom.TelecomRepository
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import com.chooloo.www.chooloolib.utils.parseDialpadText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialpadViewModel @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val audioRepository: AudioRepository,
    private val recentRepository: RecentRepository,
    private val telecomRepository: TelecomRepository,
    private val contactRepository: ContactRepository,
    private val clipboardRepository: ClipboardRepository,
    private val preferenceRepository: PreferenceRepository,
) : BaseViewModel() {
    data class UiState(
        val value: String = "",
        val deleteEnabled: Boolean = false,
        val addContactEnabled: Boolean = false,
        val suggestions: List<ContactData> = listOf()
    )

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setNumber(number: String) {
        setValue { number.parseDialpadText() }
    }

    private fun setValue(changer: (prev: String) -> String) {
        _uiState.update {
            val updated = changer(it.value)
            it.copy(
                value = updated,
                deleteEnabled = updated.isNotEmpty(),
                addContactEnabled = updated.isNotEmpty()
            )
        }
    }

    fun onCallClick() {
        val currentValue = _uiState.value.value
        viewModelScope.launch {
            if (currentValue.isNotEmpty()) {
                telecomRepository.callNumber(currentValue)
            } else {
                val lastOutgoingCall = recentRepository.getLastOutgoingCall()
                if (lastOutgoingCall.isNotEmpty()) {
                    setValue { lastOutgoingCall }
                }
            }
        }
    }

    fun onTextPasted() {
        setValue { (clipboardRepository.lastCopiedText ?: "").parseDialpadText() }
    }

    fun onDeleteClick() {
        setValue { it.dropLast(1) }
    }

    fun onDeleteLongClick() {
        setValue { "" }
    }

    fun onAddContactClick() {
        viewModelScope.launch {
            contactRepository.createContact(uiState.value.value)
        }
    }

    fun onKeyClick(key: String) {
        viewModelScope.launch {
            if (preferenceRepository.isDialpadTonesEnabled.value) {
                audioRepository.playToneByChar(key[0])
            }
            if (preferenceRepository.isDialpadVibrateEnabled.value) {
                audioRepository.vibrate(AudioRepository.SHORT_VIBRATE_LENGTH)
            }
        }
        setValue {
            val newValue = it + key
            viewModelScope.launch {
                telecomRepository.handleSpecialChars(newValue)
            }
            if (newValue.isNotEmpty()) {
                viewModelScope.launch {
                    val suggestions = phoneRepository.getPhones(filter = newValue).map { phone ->
                        ContactData(
                            id = phone.contactId,
                            number = phone.number,
                            name = phone.displayName,
                            isFavorite = phone.starred,
                            imageUri = phone.photoUri?.let(Uri::parse)
                        )
                    }
                    _uiState.update { state -> state.copy(suggestions = suggestions) }
                }
            } else {
                _uiState.update { state -> state.copy(suggestions = emptyList()) }
            }
            newValue
        }
    }

    fun onKeyLongClick(key: String) {
        when (key) {
            "0" -> setValue { "$it+" }
            "1" -> viewModelScope.launch { telecomRepository.callVoicemail() }
        }
    }
}
