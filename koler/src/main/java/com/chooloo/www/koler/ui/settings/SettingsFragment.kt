package com.chooloo.www.koler.ui.settings

import com.chooloo.www.chooloolib.ui.settings.SettingsFragment

class SettingsFragment : SettingsFragment(), SettingsContract.View {
    override val controller by lazy { SettingsController(this) }

    companion object {
        fun newInstance() = com.chooloo.www.koler.ui.settings.SettingsFragment()
    }
}