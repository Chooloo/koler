package com.chooloo.www.koler.ui.settings

import com.chooloo.www.chooloolib.ui.settings.SettingsContract as ChoolooSettingsContract

interface SettingsContract : ChoolooSettingsContract {
    interface View : ChoolooSettingsContract.View {

    }

    interface Controller : ChoolooSettingsContract.Controller {
    }
}