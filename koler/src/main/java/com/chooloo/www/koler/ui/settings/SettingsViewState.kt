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
    private val preferencesInteractor: PreferencesInteractor,
    private val permissionsInteractor: PermissionsInteractor,
) :
    SettingsViewState(
        colorsInteractor,
        navigationsInteractor,
        preferencesInteractor
    ) {

    val askForShowBlockedEvent = LiveEvent()
    val askForDefaultPageEvent = LiveEvent()
    val askForShouldAsmSimEvent = LiveEvent()


    override fun attach() {
        super.attach()
        menuResList.value = (menuResList.value ?: emptyList<Int>()) + arrayListOf(R.menu.menu_koler)
    }

    override fun onMenuItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_koler_ask_sim -> askForShouldAsmSimEvent.call()
            R.id.menu_koler_default_page -> askForDefaultPageEvent.call()
            R.id.menu_koler_show_blocked -> permissionsInteractor.runWithDefaultDialer {
                askForShowBlockedEvent.call()
            }
            else -> super.onMenuItemClick(menuItem)
        }
    }

    fun onShowBlockedResponse(response: Boolean) {
        preferencesInteractor.isShowBlocked = response
    }

    fun onDefaultPageResponse(response: Page) {
        preferencesInteractor.defaultPage = response
    }

    fun onShouldAskSimResponse(response: Boolean) {
        preferencesInteractor.isAskSim = response
    }
}
