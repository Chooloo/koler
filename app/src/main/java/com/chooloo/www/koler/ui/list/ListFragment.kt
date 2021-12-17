package com.chooloo.www.koler.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.chooloo.www.koler.adapter.ListAdapter
import com.chooloo.www.koler.databinding.ItemsBinding
import com.chooloo.www.koler.ui.base.BaseFragment

abstract class ListFragment<ItemType, Adapter : ListAdapter<ItemType>> :
    BaseFragment(),
    ListContract.View<ItemType> {

    protected val binding by lazy { ItemsBinding.inflate(layoutInflater) }

    override var isScrollerVisible: Boolean
        get() = binding.itemsScrollView.fastScroller.visibility == VISIBLE
        set(value) {
            binding.itemsScrollView.fastScroller.visibility = if (value) VISIBLE else GONE
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onSetup() {
        binding.itemsScrollView.fastScroller.setPadding(0, 0, 30, 0)
        args.getString(ARG_FILTER)?.let { controller.applyFilter(it) }
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


    companion object {
        const val ARG_FILTER = "filter"
    }


    abstract val controller: ListController<ItemType, out ListFragment<ItemType, Adapter>>
}