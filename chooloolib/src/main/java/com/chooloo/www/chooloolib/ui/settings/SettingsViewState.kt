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
    protected val colors: ColorsInteractor,
    protected val navigations: NavigationsInteractor,
    protected val preferences: PreferencesInteractor
) :
    BaseViewState() {

    open val menuResList = listOf(R.menu.menu_chooloo)

    val askForCompactEvent = LiveEvent()
    val askForThemeModeEvent = LiveEvent()
    val askForAnimationsEvent = LiveEvent()
    val askForColorEvent = DataLiveEvent<Int>()


    open fun onMenuItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.menu_chooloo_rate -> navigations.rateApp()
            R.id.menu_chooloo_theme_mode -> askForThemeModeEvent.call()
            R.id.menu_chooloo_compact_mode -> askForCompactEvent.call()
            R.id.menu_chooloo_animations -> askForAnimationsEvent.call()
            R.id.menu_chooloo_email -> navigations.sendEmail()
            R.id.menu_chooloo_report_bugs -> navigations.reportBug()
            R.id.menu_chooloo_accent_color -> askForColorEvent.call(R.array.accent_colors)
        }
    }

    fun onColorResponse(color: Int) {
        preferences.accentTheme = when (color) {
            colors.getColor(R.color.red_background) -> RED
            colors.getColor(R.color.blue_background) -> BLUE
            colors.getColor(R.color.green_background) -> GREEN
            colors.getColor(R.color.orange_background) -> ORANGE
            colors.getColor(R.color.purple_background) -> PURPLE
            else -> DEFAULT
        }
        navigations.goToLauncherActivity()
    }

    fun onCompactResponse(response: Boolean) {
        preferences.isCompact = response
        navigations.goToLauncherActivity()
    }

    fun onAnimationsResponse(response: Boolean) {
        preferences.isAnimations = response
    }

    fun onThemeModeResponse(response: ThemeMode) {
        preferences.themeMode = response
        navigations.goToLauncherActivity()
    }
}