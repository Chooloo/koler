package com.chooloo.www.koler.ui.settings

import android.view.MenuItem
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BaseMenuFragment

class SettingsFragment : BaseMenuFragment(), SettingsContract.View {
    private val controller by lazy { SettingsController(this) }
    override val title by lazy { getString(R.string.settings) }

    override fun onSetup() {
        super.onSetup()
        setMenuRes(R.menu.menu_main)
    }

    override fun onMenuItemClick(menuItem: MenuItem) {
        controller.onMenuItemClick(menuItem)
    }


    companion object {
        const val TAG = "settings_fragment"

        fun newInstance() = SettingsFragment()
    }
}