package com.chooloo.www.koler.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.chooloolib.ui.settings.SettingsViewState
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.koler.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewState @Inject constructor(
    colorsInteractor: ColorsInteractor,
    navigationsInteractor: NavigationsInteractor,
    private val preferences: PreferencesInteractor,
    private val permissionsInteractor: PermissionsInteractor,
) :
    SettingsViewState(
        colorsInteractor,
        navigationsInteractor,
        preferences
    ) {

    override val menuResList = arrayListOf(R.menu.menu_koler) + super.menuResList

    val askForShowBlockedEvent = LiveEvent()
    val askForDefaultPageEvent = LiveEvent()
    val askForDialpadTonesEvent = LiveEvent()
    val askForDialpadVibrateEvent = LiveEvent()


    override fun onMenuItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_koler_default_page -> askForDefaultPageEvent.call()
            R.id.menu_koler_dialpad_tones -> askForDialpadTonesEvent.call()
            R.id.menu_koler_dialpad_vibrate -> askForDialpadVibrateEvent.call()
            R.id.menu_koler_show_blocked -> permissionsInteractor.runWithDefaultDialer {
                askForShowBlockedEvent.call()
            }
            else -> super.onMenuItemClick(menuItem)
        }
    }

    fun onShowBlocked(response: Boolean) {
        preferences.isShowBlocked = response
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
}
