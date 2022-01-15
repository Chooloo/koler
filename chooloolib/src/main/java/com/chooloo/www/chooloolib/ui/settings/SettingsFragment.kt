package com.chooloo.www.chooloolib.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.base.BaseMenuFragment

open class SettingsFragment : BaseMenuFragment(), SettingsContract.View {
    override val title by lazy { getString(R.string.settings) }

    protected open val controller: SettingsController<out SettingsFragment> by lazy {
        SettingsController(this)
    }


    override fun onSetup() {
        super.onSetup()
        controller.initialize()
    }
    
    override fun onMenuItemClick(menuItem: MenuItem) {
        controller.onMenuItemClick(menuItem)
    }


    companion object {
        fun newInstance() = SettingsFragment()
    }
}