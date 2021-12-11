package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.R
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.AccentTheme.*
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Page
import com.chooloo.www.koler.ui.base.BaseController

class SettingsController<V : SettingsContract.View>(view: V) :
    BaseController<V>(view),
    SettingsContract.Controller<V> {

    override fun refresh() {
        boundComponent.navigationInteractor.goToMainActivity()
    }


    override fun onClickedRate() {
        boundComponent.navigationInteractor.goToRateApp()
    }

    override fun onClickedEmail() {
        boundComponent.navigationInteractor.goToSendEmail()
    }

    override fun onClickedColor() {
        view.openColorPicker()
    }

    override fun onClickedDonate() {
        boundComponent.navigationInteractor.goToDonatePage()
    }

    override fun onClickedReport() {
        boundComponent.navigationInteractor.goToReportBugPage()
    }

    override fun onClickedManageBlocked() {
        boundComponent.navigationInteractor.goToManageBlockedNumbers()
    }


    override fun onSelectedColor(color: Int) {
        boundComponent.preferencesInteractor.accentTheme = when (color) {
            boundComponent.colorInteractor.getColor(R.color.red_background) -> RED
            boundComponent.colorInteractor.getColor(R.color.blue_background) -> BLUE
            boundComponent.colorInteractor.getColor(R.color.green_background) -> GREEN
            boundComponent.colorInteractor.getColor(R.color.orange_background) -> ORANGE
            boundComponent.colorInteractor.getColor(R.color.purple_background) -> PURPLE
            else -> DEFAULT
        }
        refresh()
    }

    override fun onSelectedDefaultPage(pageKey: String) {
        boundComponent.preferencesInteractor.defaultPage = Page.fromKey(pageKey)
        refresh()
    }


    override fun onToggledRecords(isToggle: Boolean) {
        boundComponent.preferencesInteractor.isRecords = isToggle
    }

    override fun onToggledAnimation(isToggle: Boolean) {
        boundComponent.preferencesInteractor.isAnimations = isToggle
        refresh()
    }

    override fun onToggledCompactMode(isToggle: Boolean) {
        boundComponent.preferencesInteractor.isCompact = isToggle
        refresh()
    }

    override fun onToggledScrollIndicator(isToggle: Boolean) {
        boundComponent.preferencesInteractor.isScrollIndicator = isToggle
        refresh()
    }

    override fun onToggledAskSim(isToggle: Boolean) {
        boundComponent.preferencesInteractor.isAskSim = isToggle
    }
}