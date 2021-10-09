package com.chooloo.www.koler.interactor.dialog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.chooloo.www.koler.interactor.base.BaseInteractor

interface DialogInteractor : BaseInteractor<DialogInteractor.Listener> {
    interface Listener

    fun askForChoice(
        choices: Array<String>,
        @DrawableRes iconRes: Int,
        @StringRes titleRes: Int,
        choiceCallback: (String?, Int) -> Unit,
        cancelCallback: (() -> Unit?)? = null
    )
}