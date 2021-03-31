package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.util.preferences.KolerPreferences.Companion.AccentTheme.*

class SettingsPresenter<V : SettingsContract.View> : BasePresenter<V>(),
    SettingsContract.Presenter<V> {
    override fun refresh() {
        mvpView?.goToMainActivity()
    }

    override fun onClickedRate() = run {
        mvpView?.rateApp()
        true
    }

    override fun onClickedEmail() = run {
        mvpView?.sendEmail()
        true
    }

    override fun onClickedColor() = run {
        mvpView?.openColorPicker()
        true
    }

    override fun onClickedDonate() = run {
        mvpView?.donate()
        true
    }

    override fun onClickedReport() = run {
        mvpView?.reportBug()
        true
    }

    override fun onSelectedColor(color: Int) = run {
        mvpView?.setPrefAccentTheme(
            when (color) {
                mvpView?.getColor(R.color.blue_background) -> BLUE
                mvpView?.getColor(R.color.red_background) -> RED
                mvpView?.getColor(R.color.orange_background) -> ORANGE
                mvpView?.getColor(R.color.green_background) -> GREEN
                mvpView?.getColor(R.color.purple_background) -> PURPLE
                else -> BLUE
            }
        )
        mvpView?.goToMainActivity()
        true
    }

    override fun onSelectedSim(newValue: Any?) = run {
        true
    }

    override fun onSelectedRecordFormat(newValue: Any?) = run {
        true
    }

    override fun onToggledAnimation(isToggle: Boolean) = run {
        mvpView?.setPrefAnimations(isToggle)
        refresh()
        true
    }

    override fun onToggledCompactMode(isToggle: Boolean) = run {
        mvpView?.setPrefCompact(isToggle)
        refresh()
        true
    }
}