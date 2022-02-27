package com.chooloo.www.chooloolib.interactor.dialog

import android.Manifest.permission.READ_PHONE_STATE
import android.telecom.PhoneAccountHandle
import android.telecom.PhoneAccountSuggestion
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresPermission
import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.ThemeMode
import com.chooloo.www.chooloolib.model.SimAccount

interface DialogsInteractor : BaseInteractor<DialogsInteractor.Listener> {
    interface Listener

    fun askForBoolean(@StringRes titleRes: Int, callback: (result: Boolean) -> Unit)

    fun askForValidation(@StringRes titleRes: Int, callback: (result: Boolean) -> Unit)

    fun askForChoice(
        choices: List<String>,
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int? = null,
        choiceCallback: (String) -> Unit
    )

    fun <T> askForChoice(
        choices: List<T>,
        choiceToString: (T) -> String,
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int? = null,
        choiceCallback: (T) -> Unit
    )

    fun askForColor(
        @ArrayRes colorsArrayRes: Int,
        callback: (selectedColor: Int) -> Unit,
        noColorOption: Boolean = false,
        @ColorInt selectedColor: Int? = null
    )

    @RequiresPermission(READ_PHONE_STATE)
    fun askForSim(callback: (SimAccount?) -> Unit)
    fun askForDefaultPage(callback: (Page) -> Unit)
    fun askForThemeMode(callback: (ThemeMode) -> Unit)
    fun askForRoute(callback: (CallAudiosInteractor.AudioRoute) -> Unit)
    fun askForPhoneAccountHandle(
        phonesAccountHandles: List<PhoneAccountHandle>,
        callback: (PhoneAccountHandle) -> Unit
    )

    fun askForPhoneAccountSuggestion(
        phoneAccountSuggestions: List<PhoneAccountSuggestion>,
        callback: (PhoneAccountSuggestion) -> Unit
    )
}