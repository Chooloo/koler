package com.chooloo.www.koler.ui.settings

import com.chooloo.www.chooloolib.ui.settings.SettingsContract

interface SettingsContract : SettingsContract {
    interface View : SettingsContract.View {

    }

    interface Controller<V : View> : SettingsContract.Controller<V> {
    }
}