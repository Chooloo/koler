package com.chooloo.www.chooloolib.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.dialog.DialogsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.AccentTheme.*
import com.chooloo.www.chooloolib.ui.base.BaseController
import javax.inject.Inject

open class SettingsController<V : SettingsContract.View> @Inject constructor(
    view: V,
    private val colorsInteractor: ColorsInteractor,
    private val dialogsInteractor: DialogsInteractor,
    private val navigationsInteractor: NavigationsInteractor,
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
            R.id.menu_chooloo_rate -> navigationsInteractor.rateApp()
//            R.id.menu_chooloo_donate -> navigationInteractor.donate()
            R.id.menu_chooloo_email -> navigationsInteractor.sendEmail()
            R.id.menu_chooloo_report_bugs -> navigationsInteractor.reportBug()
            R.id.menu_chooloo_animations -> dialogsInteractor.askForAnimations {
                preferencesInteractor.isAnimations = it
            }
            R.id.menu_chooloo_compact_mode -> dialogsInteractor.askForCompact {
                preferencesInteractor.isCompact = it
            }
            R.id.menu_chooloo_accent_color -> dialogsInteractor.askForColor(R.array.accent_colors, {
                preferencesInteractor.accentTheme = when (it) {
                    colorsInteractor.getColor(R.color.red_background) -> RED
                    colorsInteractor.getColor(R.color.blue_background) -> BLUE
                    colorsInteractor.getColor(R.color.green_background) -> GREEN
                    colorsInteractor.getColor(R.color.orange_background) -> ORANGE
                    colorsInteractor.getColor(R.color.purple_background) -> PURPLE
                    else -> DEFAULT
                }
                navigationsInteractor.goToLauncherActivity()
            })
        }
    }
}