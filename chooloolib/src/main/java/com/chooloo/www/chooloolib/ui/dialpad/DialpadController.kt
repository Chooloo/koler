package com.chooloo.www.chooloolib.ui.dialpad

import android.view.KeyEvent.*
import com.chooloo.www.chooloolib.interactor.audio.AudioInteractor
import com.chooloo.www.chooloolib.ui.base.BaseController

open class DialpadController<V : DialpadContract.View>(view: V) :
    BaseController<V>(view),
    DialpadContract.Controller<V> {

    override fun onKeyClick(keyCode: Int) {
        component.audios.vibrate(AudioInteractor.SHORT_VIBRATE_LENGTH)
        component.audios.playToneByKey(keyCode)
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
        component.audios.vibrate()
    }

    override fun onLongDeleteClick(): Boolean {
        view.text = ""
        return true
    }

    override fun onTextChanged(text: String?) {
        view.isDeleteButtonVisible = text?.isNotEmpty() == true
    }
}