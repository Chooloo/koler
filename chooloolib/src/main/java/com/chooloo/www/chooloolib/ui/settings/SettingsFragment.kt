package com.chooloo.www.chooloolib.ui.settings

import android.view.MenuItem
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.base.BaseMenuFragment
import javax.inject.Inject

open class SettingsFragment : BaseMenuFragment(), SettingsContract.View {
    override val title by lazy { getString(R.string.settings) }

    @Inject lateinit var controller: SettingsContract.Controller<SettingsFragment>


    override fun onMenuItemClick(menuItem: MenuItem) {
        controller.onMenuItemClick(menuItem)
    }


    companion object {
        fun newInstance() = SettingsFragment()
    }
}