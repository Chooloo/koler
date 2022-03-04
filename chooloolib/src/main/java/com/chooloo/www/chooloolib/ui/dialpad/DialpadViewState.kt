package com.chooloo.www.chooloolib.ui.dialpad

import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class DialpadViewState @Inject constructor(
    private val audios: AudiosInteractor,
    private val preferences: PreferencesInteractor
) :
    BaseViewState() {

    val text = MutableLiveData("")
    val char = MutableLiveData<Char>()


    open fun onCharClick(char: Char) {
        this.char.value = char
        if (preferences.isDialpadTones) audios.playToneByChar(char)
        if (preferences.isDialpadVibrate) audios.vibrate(AudiosInteractor.SHORT_VIBRATE_LENGTH)
        onTextChanged((text.value ?: "") + char)
    }

    open fun onLongKeyClick(char: Char) = true


    protected open fun onTextChanged(text: String) {
        this.text.value = text
    }
}

