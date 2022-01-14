package com.chooloo.www.chooloolib.interactor.screen

import android.view.MotionEvent
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor

interface ScreenInteractor : BaseInteractor<ScreenInteractor.Listener> {
    interface Listener

    fun disableKeyboard()
    fun setShowWhenLocked()
    fun ignoreEditTextFocus(event: MotionEvent)
}