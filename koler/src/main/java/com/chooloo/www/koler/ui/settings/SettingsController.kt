package com.chooloo.www.koler.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.ui.settings.SettingsController
import com.chooloo.www.koler.R
import javax.inject.Inject

class SettingsController @Inject constructor(
    view: SettingsContract.View,
    colorsInteractor: ColorsInteractor,
    navigationsInteractor: NavigationsInteractor,
    private val dialogsInteractor: DialogsInteractor,
    private val preferencesInteractor: PreferencesInteractor,
    private val permissionsInteractor: PermissionsInteractor,
) :
    SettingsController(
        view,
        colorsInteractor,
        dialogsInteractor,
        navigationsInteractor,
        preferencesInteractor
    ),
    SettingsContract.Controller {

    override val menuResList = super.menuResList + listOf(R.menu.menu_koler)

    override fun onMenuItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_koler_ask_sim -> dialogsInteractor.askForShouldAskSim {
                preferencesInteractor.isAskSim = it
            }
            R.id.menu_koler_default_page -> dialogsInteractor.askForDefaultPage {
                preferencesInteractor.defaultPage = it
            }
            R.id.menu_koler_show_blocked -> permissionsInteractor.runWithDefaultDialer {
                dialogsInteractor.askForShowBlock {
                    preferencesInteractor.isShowBlocked = it
                }
            }
            R.id.menu_koler_show_blocked -> component.permissions.runWithDefaultDialer {
                component.dialogs.askForShowBlock {
                    component.preferences.isShowBlocked = it
                }
            }
            else -> super.onMenuItemClick(menuItem)
        }
    }
}
