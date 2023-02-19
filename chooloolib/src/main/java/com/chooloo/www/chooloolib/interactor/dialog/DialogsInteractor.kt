package com.chooloo.www.chooloolib.interactor.dialog

import android.Manifest.permission.READ_PHONE_STATE
import android.telecom.PhoneAccountHandle
import android.telecom.PhoneAccountSuggestion
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresPermission
import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.data.model.SimAccount
import com.chooloo.www.chooloolib.interactor.base.BaseInteractor
import com.chooloo.www.chooloolib.interactor.callaudio.CallAudiosInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.chooloolib.interactor.theme.ThemesInteractor.ThemeMode

interface DialogsInteractor : BaseInteractor<DialogsInteractor.Listener> {
    interface Listener

    fun askForBoolean(@StringRes titleRes: Int, callback: (result: Boolean) -> Unit)

    fun askForValidation(@StringRes titleRes: Int, callback: (result: Boolean) -> Unit)

    fun askForChoice(
        choices: List<String>,
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int? = null,
        selectedChoiceIndex: Int? = null,
        choiceCallback: (String) -> Boolean
    )

    fun <T> askForChoice(
        choices: List<T>,
        choiceToString: (T) -> String,
        @StringRes titleRes: Int,
        @StringRes subtitleRes: Int? = null,
        selectedChoice: T? = null,
        choiceCallback: (T) -> Boolean
    )

    fun askForColor(
        @ArrayRes colorsArrayRes: Int,
        callback: (selectedColor: Int) -> Unit,
        noColorOption: Boolean = false,
        @ColorInt selectedColor: Int? = null
    )

    @RequiresPermission(READ_PHONE_STATE)
    fun askForSim(callback: (SimAccount?) -> Boolean)
    fun askForDefaultPage(callback: (Page) -> Boolean)
    fun askForThemeMode(callback: (ThemeMode) -> Boolean)
    fun askForIncomingCallMode(callback: (IncomingCallMode) -> Boolean)
    fun askForRoute(callback: (CallAudiosInteractor.AudioRoute) -> Boolean)
    fun askForPhoneAccountHandle(
        phonesAccountHandles: List<PhoneAccountHandle>,
        callback: (PhoneAccountHandle) -> Boolean
    )

    fun askForPhoneAccountSuggestion(
        phoneAccountSuggestions: List<PhoneAccountSuggestion>,
        callback: (PhoneAccountSuggestion) -> Boolean
    )
}