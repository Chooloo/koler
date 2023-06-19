package com.chooloo.www.chooloolib.ui.dialer

import android.Manifest
import android.content.ClipboardManager
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.data.model.ContactAccount
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.dialpad.DialpadViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.chooloolib.util.MutableDataLiveEvent
import com.chooloo.www.chooloolib.util.MutableLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DialerViewState @Inject constructor(
    audios: AudiosInteractor,
    preferences: PreferencesInteractor,
    clipboardManager: ClipboardManager,
    permissions: PermissionsInteractor,
    private val recents: RecentsInteractor,
    private val navigations: NavigationsInteractor,
) :
    DialpadViewState(permissions, audios, preferences, clipboardManager) {

    override val requiredPermissions = listOf(Manifest.permission.CALL_PHONE)

    private val _isSuggestionsVisible = MutableLiveData(false)
    private val _isDeleteButtonVisible = MutableLiveData<Boolean>()
    private val _isAddContactButtonVisible = MutableLiveData<Boolean>()
    private val _callVoicemailEvent = MutableLiveEvent()
    private val _callNumberEvent = MutableDataLiveEvent<String>()

    val isSuggestionsVisible = _isSuggestionsVisible as MutableLiveData<Boolean>
    val isDeleteButtonVisible = _isDeleteButtonVisible as MutableLiveData<Boolean>
    val isAddContactButtonVisible = _isAddContactButtonVisible as MutableLiveData<Boolean>
    val callVoicemailEvent = _callVoicemailEvent as LiveEvent
    val callNumberEvent = _callNumberEvent as DataLiveEvent<String>


    override fun onLongKeyClick(char: Char) = when (char) {
        '0' -> {
            onTextChanged(text.value + "+")
            true
        }
        '1' -> {
            _callVoicemailEvent.call()
            true
        }
        else -> super.onLongKeyClick(char)
    }

    override fun onTextChanged(text: String) {
        isDeleteButtonVisible.value = text.isNotEmpty()
        isAddContactButtonVisible.value = text.isNotEmpty()
        if (text.isEmpty()) isSuggestionsVisible.value = false
        super.onTextChanged(text)
    }

    fun onDeleteClick() {
        _text.value?.dropLast(1)?.let(this::onTextChanged)
    }

    fun onLongDeleteClick(): Boolean {
        onTextChanged("")
        _text.value = ""
        return true
    }

    fun onCallClick() {
        if (text.value?.isEmpty() == true) {
            _text.value = recents.getLastOutgoingCall()
        } else {
            _text.value?.let(_callNumberEvent::call)
            onFinish()
        }
    }

    fun onAddContactClick() {
        _text.value?.let(navigations::addContact)
    }

    fun onSuggestionsChanged(contacts: List<ContactAccount>) {
        isSuggestionsVisible.value = contacts.isNotEmpty() && _text.value?.isNotEmpty() == true
    }
}