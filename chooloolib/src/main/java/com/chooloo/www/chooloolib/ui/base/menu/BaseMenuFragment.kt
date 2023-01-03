package com.chooloo.www.chooloolib.ui.base.menu

import android.annotation.SuppressLint
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.adapter.MenuAdapter
import com.chooloo.www.chooloolib.databinding.MenuBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("RestrictedApi")
@AndroidEntryPoint
open class BaseMenuFragment @Inject constructor() : BaseFragment<BaseViewState>() {
    override val contentView by lazy { binding.root }
    override val viewState: BaseMenuViewState by viewModels()
    private val binding by lazy { MenuBinding.inflate(layoutInflater) }
    protected var menu: MenuBuilder? = null

    @Inject lateinit var adapter: MenuAdapter


    override fun onSetup() {
        adapter.setOnItemClickListener(::onMenuItemClick)

        viewState.apply {
            title.observe(this@BaseMenuFragment) {
                binding.menuTitle.text = it
                binding.menuTitle.isVisible = it.isNotEmpty()
            }

            subtitle.observe(this@BaseMenuFragment) {
                binding.menuSubtitle.text = it
                binding.menuSubtitle.isVisible = it.isNotEmpty()
            }
        }

        setMenuResList(viewState.menuResList)
        binding.menuRecyclerView.adapter = adapter
    }

    private fun refreshMenu() {
        menu?.visibleItems?.toList()?.let { adapter.items = it }
    }

    protected open fun onMenuItemClick(menuItem: MenuItem) {
        viewState.onMenuItemClick(menuItem.itemId)
    }

    protected fun changeItemVisibility(itemId: Int, isVisible: Boolean) {
        menu?.findItem(itemId)?.let { it.isVisible = isVisible }
        refreshMenu()
    }

    private fun setMenuResList(menuResList: List<Int>) {
        menu = MenuBuilder(baseActivity).apply {
            menuResList.forEach { MenuInflater(baseActivity).inflate(it, this) }
        }
        refreshMenu()
    }


    companion object {
        const val ARG_TITLE_RES = "title_res"
        const val ARG_SUBTITLE_RES = "subtitle_res"
    }
}