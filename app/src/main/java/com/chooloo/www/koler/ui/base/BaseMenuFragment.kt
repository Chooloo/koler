package com.chooloo.www.koler.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.view.menu.MenuBuilder
import com.chooloo.www.koler.adapter.MenuAdapter
import com.chooloo.www.koler.databinding.MenuBinding

@SuppressLint("RestrictedApi")
open class BaseMenuFragment : BaseFragment() {
    private var _onMenuItemClickListener: (MenuItem) -> Unit = {}
    private val adapter by lazy { MenuAdapter(component) }
    protected open val title by lazy { args.getString(ARG_TITLE) }
    private val binding by lazy { MenuBinding.inflate(layoutInflater) }

    override val contentView by lazy { binding.root }


    override fun onSetup() {
        adapter.setOnItemClickListener {
            _onMenuItemClickListener.invoke(it)
            onMenuItemClick(it)
        }
        binding.apply {
            menuTitle.text = title
            menuRecyclerView.adapter = adapter
        }
    }


    fun setMenu(menu: MenuBuilder) {
        menu.visibleItems.toList().let { adapter.items = it }
    }

    fun setMenuRes(menuRes: Int) {
        setMenu(MenuBuilder(baseActivity).apply {
            MenuInflater(baseActivity).inflate(menuRes, this)
        })
    }

    fun setOnMenuItemClickListener(onMenuItemClickListener: (menuItem: MenuItem) -> Unit) {
        _onMenuItemClickListener = onMenuItemClickListener
    }

    protected open fun onMenuItemClick(menuItem: MenuItem) {}


    companion object {
        const val ARG_TITLE = "title"

        fun newInstance(title: String) = BaseMenuFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TITLE, title)
            }
        }
    }
}