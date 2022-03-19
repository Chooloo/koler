package com.chooloo.www.chooloolib.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.color.ColorsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.AccentTheme.*
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor.Companion.ThemeMode
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class SettingsViewState @Inject constructor(
    private val colorsInteractor: ColorsInteractor,
    private val navigationsInteractor: NavigationsInteractor,
    private val preferencesInteractor: PreferencesInteractor
) :
    BaseViewState() {

    open val menuResList = listOf(R.menu.menu_chooloo)

    val askForCompactEvent = LiveEvent()
    val askForThemeModeEvent = LiveEvent()
    val askForAnimationsEvent = LiveEvent()
    val askForColorEvent = DataLiveEvent<Int>()


    open fun onMenuItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_chooloo_rate -> navigationsInteractor.rateApp()
            R.id.menu_chooloo_theme_mode -> askForThemeModeEvent.call()
            R.id.menu_chooloo_compact_mode -> askForCompactEvent.call()
            R.id.menu_chooloo_animations -> askForAnimationsEvent.call()
            R.id.menu_chooloo_email -> navigationsInteractor.sendEmail()
            R.id.menu_chooloo_report_bugs -> navigationsInteractor.reportBug()
            R.id.menu_chooloo_accent_color -> askForColorEvent.call(R.array.accent_colors)
        }
    }

    fun onColorResponse(color: Int) {
        preferencesInteractor.accentTheme = when (color) {
            colorsInteractor.getColor(R.color.red_background) -> RED
            colorsInteractor.getColor(R.color.blue_background) -> BLUE
            colorsInteractor.getColor(R.color.green_background) -> GREEN
            colorsInteractor.getColor(R.color.orange_background) -> ORANGE
            colorsInteractor.getColor(R.color.purple_background) -> PURPLE
            else -> DEFAULT
        }
        navigationsInteractor.goToLauncherActivity()
    }

    fun onCompactResponse(response: Boolean) {
        preferencesInteractor.isCompact = response
        navigationsInteractor.goToLauncherActivity()
    }

    fun onAnimationsResponse(response: Boolean) {
        preferencesInteractor.isAnimations = response
    }

    fun onThemeModeResponse(response: ThemeMode) {
        preferencesInteractor.themeMode = response
        navigationsInteractor.goToLauncherActivity()
    }
}