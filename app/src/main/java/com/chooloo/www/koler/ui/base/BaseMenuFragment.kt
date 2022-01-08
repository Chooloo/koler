package com.chooloo.www.koler.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import androidx.annotation.StringRes
import androidx.appcompat.view.menu.MenuBuilder
import com.chooloo.www.koler.adapter.MenuAdapter
import com.chooloo.www.koler.databinding.MenuBinding

@SuppressLint("RestrictedApi")
open class BaseMenuFragment : BaseFragment() {
    private var _onMenuItemClickListener: (MenuItem) -> Unit = {}
    private val adapter by lazy { MenuAdapter(component) }
    private val binding by lazy { MenuBinding.inflate(layoutInflater) }

    protected open val title by lazy { getString(args.getInt(ARG_TITLE_RES)) }
    protected open val subtitle by lazy {
        val subtitleRes = args.getInt(ARG_SUBTITLE_RES, -1)
        if (subtitleRes == -1) {
            null
        } else {
            getString(subtitleRes)
        }
    }

    override val contentView by lazy { binding.root }


    override fun onSetup() {
        adapter.setOnItemClickListener {
            _onMenuItemClickListener.invoke(it)
            onMenuItemClick(it)
        }
        binding.apply {
            menuRecyclerView.adapter = adapter
            menuTitle.text = title
            subtitle?.let { menuSubtitle.text = it }
            if (subtitle == null) {
                menuSubtitle.visibility = GONE
            }
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
        const val ARG_TITLE_RES = "title_res"
        const val ARG_SUBTITLE_RES = "subtitle_res"

        fun newInstance(@StringRes titleRes: Int, @StringRes subtitleRes: Int? = null) =
            BaseMenuFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TITLE_RES, titleRes)
                    subtitleRes?.let { putInt(ARG_SUBTITLE_RES, it) }
                }
            }
    }
}