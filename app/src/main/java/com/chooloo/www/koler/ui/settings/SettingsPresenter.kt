package com.chooloo.www.koler.ui.settings

import com.chooloo.www.koler.ui.base.BasePresenter

class SettingsPresenter<V : SettingsContract.View> : BasePresenter<V>(),
    SettingsContract.Presenter<V> {
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