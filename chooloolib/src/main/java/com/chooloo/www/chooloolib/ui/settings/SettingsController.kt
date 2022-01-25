package com.chooloo.www.chooloolib.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.color.ColorInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.AccentTheme.*
import com.chooloo.www.chooloolib.ui.base.BaseController
import javax.inject.Inject

open class SettingsController<V : SettingsContract.View> @Inject constructor(
    view: V,
    private val colorInteractor: ColorInteractor,
    private val dialogsInteractor: DialogsInteractor,
    private val navigationInteractor: NavigationInteractor,
    private val preferencesInteractor: PreferencesInteractor
) :
    BaseController<V>(view),
    SettingsContract.Controller<V> {

    protected open val menuResList = listOf(R.menu.menu_chooloo)

    override fun onStart() {
        super.onStart()
        view.setMenuResList(menuResList)
    }

    override fun onMenuItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_chooloo_rate -> navigationInteractor.rateApp()
//            R.id.menu_chooloo_donate -> navigationInteractor.donate()
            R.id.menu_chooloo_email -> navigationInteractor.sendEmail()
            R.id.menu_chooloo_report_bugs -> navigationInteractor.reportBug()
            R.id.menu_chooloo_animations -> dialogsInteractor.askForAnimations {
                preferencesInteractor.isAnimations = it
            }
            R.id.menu_chooloo_compact_mode -> dialogsInteractor.askForCompact {
                preferencesInteractor.isCompact = it
            }
            R.id.menu_chooloo_accent_color -> dialogsInteractor.askForColor(R.array.accent_colors, {
                preferencesInteractor.accentTheme = when (it) {
                    colorInteractor.getColor(R.color.red_background) -> RED
                    colorInteractor.getColor(R.color.blue_background) -> BLUE
                    colorInteractor.getColor(R.color.green_background) -> GREEN
                    colorInteractor.getColor(R.color.orange_background) -> ORANGE
                    colorInteractor.getColor(R.color.purple_background) -> PURPLE
                    else -> DEFAULT
                }
                navigationInteractor.goToLauncherActivity()
            })
        }
    }
}