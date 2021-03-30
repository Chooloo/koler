package com.chooloo.www.koler.ui.menu

import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import com.chooloo.www.koler.adapter.MenuAdapter
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.ui.list.ListFragment

class MenuFragment : ListFragment<MenuAdapter>(), MenuContract.View {
    private var _onMenuItemClickListener: (MenuItem) -> Unit? = {}

    companion object {
        const val ARG_MENU_LAYOUT = "menu_layout"

        fun newInstance(@MenuRes menuLayout: Int) = MenuFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MENU_LAYOUT, menuLayout)
            }
        }
    }

    override fun onGetAdapter() = MenuAdapter().apply {
        setOnItemClickListener { _onMenuItemClickListener.invoke(it) }
    }

    fun setOnMenuItemClickListener(onMenuItemClickListener: (MenuItem) -> Unit?) {
        _onMenuItemClickListener = onMenuItemClickListener
    }

    override fun onAttachData() {
        PopupMenu(_activity, null).menu.also { m ->
            MenuInflater(_activity).inflate(argsSafely.getInt(ARG_MENU_LAYOUT), m)
            listAdapter.data = ListBundle((0 until m.size()).map { m.getItem(it) }.toTypedArray())
        }
    }
}