package com.chooloo.www.chooloolib.ui.dialer

import android.content.ClipboardManager
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
    private val clipboardManager: ClipboardManager,
    private val navigations: NavigationsInteractor
) :
    DialpadViewState(audios, clipboardManager, preferences) {

    val callVoicemailEvent = LiveEvent()
    val callNumberEvent = DataLiveEvent<String>()
    val isSuggestionsVisible = MutableLiveData(false)
    val isDeleteButtonVisible = MutableLiveData<Boolean>()
    val isAddContactButtonVisible = MutableLiveData<Boolean>()


    override fun onLongKeyClick(char: Char) = when (char) {
        '0' -> {
            var pos = editTextRef.value?.selectionStart!!
            onTextChanged((text.value ?: "").replaceRange(pos, pos, "+"))
            editTextRef.value?.setSelection(pos+1)
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
        // note: overwork this code if the dev are better in kotin :D

        var pos = editTextRef.value!!.selectionStart
        if(pos == 0 && editTextRef.value!!.selectionEnd == text.value!!.length){
             this.onLongDeleteClick();
            return;
        }

        if(pos-1 < 0) return

        text.value?.removeRange(pos-1, pos)?.let(this::onTextChanged)
        editTextRef.value?.setSelection(pos-1)
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