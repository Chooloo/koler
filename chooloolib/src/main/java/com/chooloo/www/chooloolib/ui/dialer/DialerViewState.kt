package com.chooloo.www.chooloolib.ui.dialer

import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.ui.dialpad.DialpadViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DialerViewState @Inject constructor(
    audios: AudiosInteractor,
    preferences: PreferencesInteractor,
    private val recents: RecentsInteractor,
    private val navigations: NavigationsInteractor
) :
    DialpadViewState(audios, preferences) {

    val callVoicemailEvent = LiveEvent()
    val callNumberEvent = DataLiveEvent<String>()
    val isSuggestionsVisible = MutableLiveData(false)
    val isDeleteButtonVisible = MutableLiveData<Boolean>()
    val isAddContactButtonVisible = MutableLiveData<Boolean>()


    override fun onLongKeyClick(char: Char) = when (char) {
        '0' -> {
            onTextChanged(text.value + "+")
            true
        }
        '1' -> {
            callVoicemailEvent.call()
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
        text.value?.dropLast(1)?.let(this::onTextChanged)
    }

    fun onLongDeleteClick(): Boolean {
        onTextChanged("")
        text.value = ""
        return true
    }

    fun onCallClick() {
        if (text.value?.isEmpty() == true) {
            text.value = recents.getLastOutgoingCall()
        } else {
            text.value?.let(callNumberEvent::call)
            finishEvent.call()
        }
    }

    fun onAddContactClick() {
        text.value?.let(navigations::addContact)
    }

    fun onSuggestionsChanged(contacts: List<ContactAccount>) {
        isSuggestionsVisible.value = contacts.isNotEmpty() && text.value?.isNotEmpty() == true
    }
}