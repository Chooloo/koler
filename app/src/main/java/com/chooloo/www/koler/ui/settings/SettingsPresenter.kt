package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.AccentTheme.*
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor.Companion.Page

class SettingsPresenter<V : SettingsContract.View>(mvpView: V) :
    BasePresenter<V>(mvpView),
    SettingsContract.Presenter<V> {
    
    override fun refresh() {
        view.goToMainActivity()
    }

    override fun onClickedRate() {
        view.rateApp()
    }

    override fun onClickedEmail() {
        view.sendEmail()
    }

    override fun onClickedColor() {
        view.openColorPicker()
    }

    override fun onClickedDonate() {
        view.donate()
    }

    override fun onClickedReport() {
        view.reportBug()
    }

    override fun onClickedManageBlocked() {
        view.manageBlockedNumbers()
    }

    override fun onSelectedColor(color: Int) {
        view.setPrefAccentTheme(
            when (color) {
                view.getColor(R.color.blue_background) -> BLUE
                view.getColor(R.color.red_background) -> RED
                view.getColor(R.color.orange_background) -> ORANGE
                view.getColor(R.color.green_background) -> GREEN
                view.getColor(R.color.purple_background) -> PURPLE
                else -> BLUE
            }
        )
        view.goToMainActivity()
    }

    override fun onSelectedSim(newValue: Any?) {
    }

    override fun onToggledRecords(isToggle: Boolean) {
        view.setPrefRecordsEnabled(isToggle)
    }

    override fun onSelectedRecordFormat(newValue: Any?) {
    }

    override fun onToggledAnimation(isToggle: Boolean) {
        view.setPrefAnimations(isToggle)
        refresh()
    }

    override fun onToggledCompactMode(isToggle: Boolean) {
        view.setPrefCompact(isToggle)
        refresh()
    }

    override fun onSelectedDefaultPage(pageKey: String) {
        view.setPrefDefaultPage(Page.fromKey(pageKey))
        refresh()
    }

    override fun onToggledScrollIndicator(isToggle: Boolean) {
        view.setPrefScrollIndicator(isToggle)
        refresh()
    }
}