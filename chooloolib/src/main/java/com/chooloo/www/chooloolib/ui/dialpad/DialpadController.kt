package com.chooloo.www.chooloolib.ui.dialpad

import android.view.KeyEvent.*
import com.chooloo.www.chooloolib.interactor.audio.AudiosInteractor
import com.chooloo.www.chooloolib.ui.base.BaseController
import javax.inject.Inject

open class DialpadController @Inject constructor(
    view: DialpadContract.View,
    private val audiosInteractor: AudiosInteractor
) :
    BaseController<DialpadContract.View>(view),
    DialpadContract.Controller {

    override fun onKeyClick(keyCode: Int) {
        audiosInteractor.vibrate(AudiosInteractor.SHORT_VIBRATE_LENGTH)
        audiosInteractor.playToneByKey(keyCode)
        view.invokeKey(keyCode)
    }

    override fun onLongKeyClick(keyCode: Int): Boolean {
        return when (keyCode) {
            KEYCODE_0 -> {
                onKeyClick(KEYCODE_PLUS)
                true
            }
            else -> true
        }
    }

    override fun onDeleteClick() {
        view.invokeKey(KEYCODE_DEL)
        audiosInteractor.vibrate()
    }

    override fun onLongDeleteClick(): Boolean {
        view.text = ""
        return true
    }

    override fun onTextChanged(text: String?) {
        view.isDeleteButtonVisible = text?.isNotEmpty() == true
    }
}