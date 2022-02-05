package com.chooloo.www.chooloolib.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.base.BaseMenuFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class SettingsFragment @Inject constructor() : BaseMenuFragment(), SettingsContract.View {
    override val title by lazy { getString(R.string.settings) }
    override val controller by lazy { controllerFactory.getSettingsController(this) }


    override fun onSetup() {
        super.onSetup()
        controller.init()
    }

    override fun onMenuItemClick(menuItem: MenuItem) {
        controller.onMenuItemClick(menuItem)
    }
}