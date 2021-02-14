package com.chooloo.www.koler.ui.menu

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.MenuRes
import com.chooloo.www.koler.ui.base.BaseBottomSheetDialogFragment
import com.chooloo.www.koler.ui.menu.MenuFragment.Companion.ARG_MENU_LAYOUT

class MenuBottomFragment : BaseBottomSheetDialogFragment() {
    private lateinit var _menuFragment: MenuFragment
    private var _onMenuItemClickListener: ((MenuItem) -> Unit?)? = null

    companion object {
        fun newInstance(@MenuRes menuLayout: Int) = MenuBottomFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MENU_LAYOUT, menuLayout)
            }
        }
    }

    override fun onSetup() {
        _menuFragment = MenuFragment.newInstance(argsSafely.getInt(ARG_MENU_LAYOUT)).apply {
            setOnMenuItemClickListener(_onMenuItemClickListener)
            putFragment(this)
        }
    }

    fun setOnMenuItemClickListener(onMenuItemClickListener: ((MenuItem) -> Unit?)?) {
        _onMenuItemClickListener = onMenuItemClickListener
    }
}