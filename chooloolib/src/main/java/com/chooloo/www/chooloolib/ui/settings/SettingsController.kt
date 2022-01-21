package com.chooloo.www.chooloolib.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.AccentTheme.*
import com.chooloo.www.chooloolib.ui.base.BaseController

open class SettingsController<V : SettingsContract.View>(view: V) :
    BaseController<V>(view),
    SettingsContract.Controller<V> {

    protected open val menuResList = listOf(R.menu.menu_chooloo)

    override fun onStart() {
        super.onStart()
        view.setMenuResList(menuResList)
    }

    override fun onMenuItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_chooloo_rate -> component.navigations.rateApp()
            R.id.menu_chooloo_donate -> component.navigations.donate()
            R.id.menu_chooloo_email -> component.navigations.sendEmail()
            R.id.menu_chooloo_report_bugs -> component.navigations.reportBug()
            R.id.menu_chooloo_animations -> component.dialogs.askForAnimations {
                component.preferences.isAnimations = it
            }
            R.id.menu_chooloo_compact_mode -> component.dialogs.askForCompact {
                component.preferences.isCompact = it
            }
            R.id.menu_chooloo_accent_color -> component.dialogs.askForColor(R.array.accent_colors, {
                component.preferences.accentTheme = when (it) {
                    component.colors.getColor(R.color.red_background) -> RED
                    component.colors.getColor(R.color.blue_background) -> BLUE
                    component.colors.getColor(R.color.green_background) -> GREEN
                    component.colors.getColor(R.color.orange_background) -> ORANGE
                    component.colors.getColor(R.color.purple_background) -> PURPLE
                    else -> DEFAULT
                }
                component.navigations.goToLauncherActivity()
            })
        }
    }
}