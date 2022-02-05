package com.chooloo.www.chooloolib.ui.list

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.adapter.ListAdapter
import com.chooloo.www.chooloolib.databinding.ItemsBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment

abstract class ListFragment<ItemType, Adapter : ListAdapter<ItemType>> :
    BaseFragment(),
    ListContract.View<ItemType> {

    override val contentView by lazy { binding.root }

    override var isScrollerVisible: Boolean
        get() = binding.itemsScrollView.fastScroller.isVisible
        set(value) {
            binding.itemsScrollView.fastScroller.isVisible = value
        }

    private var _onItemClickListener: (ItemType) -> Unit = {}
    private var _onItemLongClickListener: (ItemType) -> Unit = {}
    protected val binding by lazy { ItemsBinding.inflate(layoutInflater) }

    override fun onSetup() {
        binding.itemsScrollView.fastScroller.setPadding(0, 0, 30, 0)
        args.getString(ARG_FILTER)?.let { controller.applyFilter(it) }
        controller.setOnItemClickListener(_onItemClickListener)
        controller.setOnItemLongClickListener(_onItemLongClickListener)
    }

    override fun showEmpty(isShow: Boolean) {
        binding.apply {
            empty.emptyIcon.visibility = if (isShow) VISIBLE else GONE
            empty.emptyText.visibility = if (isShow) VISIBLE else GONE
            itemsScrollView.visibility = if (isShow) GONE else VISIBLE
        }
    }

    override fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            showEmpty(false)
        }
    }

    override fun setEmptyReason(@StringRes res: Int?) {
        res?.let { binding.empty.emptyText.setText(it) }
    }

    override fun setEmptyIcon(res: Int?) {
        res?.let { binding.empty.emptyIcon.setImageResource(it) }
    }

    override fun setAdapter(adapter: ListAdapter<ItemType>) {
        binding.itemsScrollView.setAdapter(adapter)
    }

    override fun setOnItemClickListener(onItemClickListener: (ItemType) -> Unit) {
        _onItemClickListener = onItemClickListener
    }

    override fun setOnItemLongClickListener(onItemLongClickListener: (ItemType) -> Unit) {
        _onItemLongClickListener = onItemLongClickListener
    }

    companion object {
        const val ARG_FILTER = "filter"
    }


    abstract override val controller: ListContract.Controller<ItemType, out ListContract.View<ItemType>>
}