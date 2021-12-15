package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.AccentTheme.*
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.koler.ui.base.BaseController

class SettingsController<V : SettingsContract.View>(view: V) :
    BaseController<V>(view),
    SettingsContract.Controller<V> {

    override fun refresh() {
        component.navigations.goToMainActivity()
    }


    override fun onClickedRate() {
        component.navigations.rateApp()
    }

    override fun onClickedEmail() {
        component.navigations.sendEmail()
    }

    override fun onClickedColor() {
        view.openColorPicker()
    }

    override fun onClickedDonate() {
        component.navigations.donate()
    }

    override fun onClickedReport() {
        component.navigations.reportBug()
    }

    override fun onClickedManageBlocked() {
        component.navigations.manageBlockedNumber()
    }


    override fun onSelectedColor(color: Int) {
        component.preferences.accentTheme = when (color) {
            component.colors.getColor(R.color.red_background) -> RED
            component.colors.getColor(R.color.blue_background) -> BLUE
            component.colors.getColor(R.color.green_background) -> GREEN
            component.colors.getColor(R.color.orange_background) -> ORANGE
            component.colors.getColor(R.color.purple_background) -> PURPLE
            else -> DEFAULT
        }
        refresh()
    }

    override fun onSelectedDefaultPage(pageKey: String) {
        component.preferences.defaultPage = Page.fromKey(pageKey)
        refresh()
    }


    override fun onToggledRecords(isToggle: Boolean) {
        component.preferences.isRecords = isToggle
    }

    override fun onToggledAnimation(isToggle: Boolean) {
        component.preferences.isAnimations = isToggle
        refresh()
    }

    override fun onToggledCompactMode(isToggle: Boolean) {
        component.preferences.isCompact = isToggle
        refresh()
    }

    override fun onToggledScrollIndicator(isToggle: Boolean) {
        component.preferences.isScrollIndicator = isToggle
        refresh()
    }

    override fun onToggledAskSim(isToggle: Boolean) {
        component.preferences.isAskSim = isToggle
    }
}