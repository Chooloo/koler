package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.ui.base.BasePresenter

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
        true
    }

    override fun onSelectedSim(newValue: Any?) = run {
        true
    }

    override fun onSelectedRecordFormat(newValue: Any?) = run {
        true
    }
}