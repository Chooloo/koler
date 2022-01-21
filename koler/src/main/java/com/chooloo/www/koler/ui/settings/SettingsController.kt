package com.chooloo.www.koler.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.ui.settings.SettingsController
import com.chooloo.www.koler.R

class SettingsController<V : SettingsContract.View>(view: V) :
    SettingsController<V>(view),
    SettingsContract.Controller<V> {

    override val menuResList = super.menuResList + listOf(R.menu.menu_koler)

    override fun onMenuItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_koler_ask_sim -> component.dialogs.askForShouldAskSim {
                component.preferences.isAskSim = it
            }
            R.id.menu_koler_default_page -> component.dialogs.askForDefaultPage {
                component.preferences.defaultPage = it
            }
            else -> super.onMenuItemClick(menuItem)
        }
    }
}
