package com.chooloo.www.koler.interactor.dialog

import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.chooloo.www.koler.interactor.base.BaseInteractor
import com.chooloo.www.koler.interactor.callaudio.CallAudioInteractor
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Page

interface DialogInteractor : BaseInteractor<DialogInteractor.Listener> {
    interface Listener

    fun askForBoolean(@StringRes titleRes: Int, callback: (result: Boolean) -> Unit)

    fun askForValidation(@StringRes titleRes: Int, callback: (result: Boolean) -> Unit)
    
    fun askForChoice(
        choices: List<String>,
        @DrawableRes iconRes: Int,
        @StringRes titleRes: Int,
        choiceCallback: (String?, Int) -> Unit,
        cancelCallback: (() -> Unit?)? = null
    )

    fun askForColor(
        @ArrayRes colorsArrayRes: Int,
        callback: (selectedColor: Int) -> Unit,
        noColorOption: Boolean = false,
        @ColorInt selectedColor: Int? = null
    )

    fun askForRoute(callback: (CallAudioInteractor.AudioRoute) -> Unit)

    fun askForDefaultPage(callback: (Page) -> Unit)
    fun askForCompact(callback: (isCompact: Boolean) -> Unit)
    fun askForAnimations(callback: (isAnimations: Boolean) -> Unit)
    fun askForShouldAskSim(callback: (shouldAskSim: Boolean) -> Unit)
}