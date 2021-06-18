package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.AccentTheme.*
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.Page

class SettingsPresenter<V : SettingsContract.View>(mvpView: V) :
    BasePresenter<V>(mvpView),
    SettingsContract.Presenter<V> {
    
    override fun refresh() {
        mvpView.goToMainActivity()
    }

    override fun onClickedRate() {
        mvpView.rateApp()
    }

    override fun onClickedEmail() {
        mvpView.sendEmail()
    }

    override fun onClickedColor() {
        mvpView.openColorPicker()
    }

    override fun onClickedDonate() {
        mvpView.donate()
    }

    override fun onClickedReport() {
        mvpView.reportBug()
    }

    override fun onClickedManageBlocked() {
        mvpView.manageBlockedNumbers()
    }

    override fun onSelectedColor(color: Int) {
        mvpView.setPrefAccentTheme(
            when (color) {
                mvpView.getColor(R.color.blue_background) -> BLUE
                mvpView.getColor(R.color.red_background) -> RED
                mvpView.getColor(R.color.orange_background) -> ORANGE
                mvpView.getColor(R.color.green_background) -> GREEN
                mvpView.getColor(R.color.purple_background) -> PURPLE
                else -> BLUE
            }
        )
        mvpView.goToMainActivity()
    }

    override fun onSelectedSim(newValue: Any?) {
    }

    override fun onToggledRecords(isToggle: Boolean) {
        mvpView.setPrefRecordsEnabled(isToggle)
    }

    override fun onSelectedRecordFormat(newValue: Any?) {
    }

    override fun onToggledAnimation(isToggle: Boolean) {
        mvpView.setPrefAnimations(isToggle)
        refresh()
    }

    override fun onToggledCompactMode(isToggle: Boolean) {
        mvpView.setPrefCompact(isToggle)
        refresh()
    }

    override fun onSelectedDefaultPage(pageKey: String) {
        mvpView.setPrefDefaultPage(Page.fromKey(pageKey))
        refresh()
    }

    override fun onToggledScrollIndicator(isToggle: Boolean) {
        mvpView.setPrefScrollIndicator(isToggle)
        refresh()
    }
}