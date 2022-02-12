package com.chooloo.www.chooloolib.ui.dialpad

import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class DialpadViewState @Inject constructor(
    private val audiosInteractor: AudiosInteractor
) :
    BaseViewState() {

    val text = MutableLiveData("")
    val char = MutableLiveData<Char>()


    open fun onCharClick(char: Char) {
        this.char.value = char
        audiosInteractor.playToneByChar(char)
        audiosInteractor.vibrate(AudiosInteractor.SHORT_VIBRATE_LENGTH)
        onTextChanged((text.value ?: "") + char)
    }

    open fun onLongKeyClick(char: Char) = when (char) {
        '0' -> {
            onTextChanged(text.value + "+")
            true
        }
        else -> true
    }


    protected open fun onTextChanged(text: String) {
        this.text.value = text
        audiosInteractor.vibrate()
    }
}

