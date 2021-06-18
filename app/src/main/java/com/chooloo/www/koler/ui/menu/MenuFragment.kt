package com.chooloo.www.koler.ui.menu

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import com.chooloo.www.koler.adapter.MenuAdapter
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.ui.list.ListFragment

open class MenuFragment : ListFragment<MenuItem, MenuAdapter>(), MenuContract.View {
    open val menuRes by lazy { argsSafely.getInt(ARG_MENU_LAYOUT) }

    override val adapter by lazy { MenuAdapter() }


    override fun onAttachData() {
        val menu = PopupMenu(baseActivity, null).menu
        MenuInflater(baseActivity).inflate(menuRes, menu)
        onAttachMenu(menu)
        adapter.data = ListBundle(ArrayList((0 until menu.size()).map { menu.getItem(it) }))
    }

    override fun onItemClick(item: MenuItem) {
    }

    override fun onItemLongClick(item: MenuItem) {
    }

    open fun onAttachMenu(menu: Menu) {
    }


    companion object {
        const val ARG_MENU_LAYOUT = "menu_layout"

        fun newInstance(@MenuRes menuLayout: Int) = MenuFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MENU_LAYOUT, menuLayout)
            }
        }
    }
}