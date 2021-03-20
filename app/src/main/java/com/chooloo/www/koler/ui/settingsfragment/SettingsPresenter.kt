package com.chooloo.www.koler.ui.settingsfragment

import com.chooloo.www.koler.ui.base.BasePresenter

class SettingsPresenter<V : SettingsFragmentContract.View> : BasePresenter<V>(),
    SettingsFragmentContract.Presenter<V> {
    override fun refresh() {
        mvpView?.goToMainActivity()
    }

    override fun onSimSelectionChanged(newValue: Any?) {
        TODO("Not yet implemented")
    }

    override fun onAppThemeSelectionChanged(color: Int) {
        TODO("Not yet implemented")
    }
}