package com.chooloo.www.chooloolib.ui.dialpad

import android.content.ClipboardManager
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.ui.widgets.DialpadEditText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class DialpadViewState @Inject constructor(
    private val audios: AudiosInteractor,
    private val clipboardManager: ClipboardManager,
    private val preferences: PreferencesInteractor
) :
    BaseViewState() {

    val text = MutableLiveData("")
    val char = MutableLiveData<Char>()
    val editTextRef = MutableLiveData<DialpadEditText>()

    open fun onCharClick(char: Char) {
        this.char.value = char
        if (preferences.isDialpadTones) audios.playToneByChar(char)
        if (preferences.isDialpadVibrate) audios.vibrate(AudiosInteractor.SHORT_VIBRATE_LENGTH)

        var pos = editTextRef.value?.selectionStart!!
        onTextChanged((text.value ?: "").replaceRange(pos, pos, char.toString()))
        editTextRef.value?.setSelection(pos+1)
    }

    open fun onLongKeyClick(char: Char) = true

    open fun onTextPasted() {
        val item = clipboardManager.primaryClip?.getItemAt(0)
        val text = item?.text.toString().replace(Regex("[^+#*0-9]"), "")
        onTextChanged(text)
    }


    protected open fun onTextChanged(text: String) {
        this.text.value = text
    }
}

