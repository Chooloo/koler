package com.chooloo.www.koler.ui.settings

import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.IncomingCallMode
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.interactor.theme.ThemesInteractor
import com.chooloo.www.chooloolib.ui.settings.SettingsViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.chooloolib.util.MutableDataLiveEvent
import com.chooloo.www.chooloolib.util.MutableLiveEvent
import com.chooloo.www.koler.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewState @Inject constructor(
    colors: ColorsInteractor,
    themes: ThemesInteractor,
    strings: StringsInteractor,
    navigations: NavigationsInteractor,
    preferences: PreferencesInteractor
) :
    SettingsViewState(themes, colors, strings, navigations, preferences) {

    override val menuResList = arrayListOf(R.menu.menu_koler) + super.menuResList

    private val _askForDefaultPageEvent = MutableLiveEvent()
    private val _askForIncomingCallModeEvent = MutableLiveEvent()
    private val _askForDialpadTonesEvent = MutableDataLiveEvent<Boolean>()
    private val _askForGroupRecentsEvent = MutableDataLiveEvent<Boolean>()
    private val _askForDialpadVibrateEvent = MutableDataLiveEvent<Boolean>()

    val askForDefaultPageEvent = _askForDefaultPageEvent as LiveEvent
    val askForIncomingCallModeEvent = _askForIncomingCallModeEvent as LiveEvent
    val askForGroupRecentsEvent = _askForGroupRecentsEvent as DataLiveEvent<Boolean>
    val askForDialpadTonesEvent = _askForDialpadTonesEvent as DataLiveEvent<Boolean>
    val askForDialpadVibrateEvent = _askForDialpadVibrateEvent as DataLiveEvent<Boolean>


    override fun onMenuItemClick(itemId: Int) {
        when (itemId) {
            R.id.menu_koler_default_page -> _askForDefaultPageEvent.call()
            R.id.menu_koler_incoming_call_mode -> _askForIncomingCallModeEvent.call()
            R.id.menu_koler_dialpad_tones -> _askForDialpadTonesEvent.call(preferences.isDialpadTones)
            R.id.menu_koler_group_recents -> _askForGroupRecentsEvent.call(preferences.isGroupRecents)
            R.id.menu_koler_dialpad_vibrate -> _askForDialpadVibrateEvent.call(preferences.isDialpadVibrate)
            else -> super.onMenuItemClick(itemId)
        }
    }


    fun onDefaultPageResponse(response: Page) {
        preferences.defaultPage = response
    }

    fun onDialpadTones(response: Boolean) {
        preferences.isDialpadTones = response
    }

    fun onDialpadVibrate(response: Boolean) {
        preferences.isDialpadVibrate = response
    }

    fun onGroupRecents(response: Boolean) {
        preferences.isGroupRecents = response
        navigations.goToLauncherActivity()
    }

    fun onIncomingCallMode(response: IncomingCallMode) {
        preferences.incomingCallMode = response
    }
}
