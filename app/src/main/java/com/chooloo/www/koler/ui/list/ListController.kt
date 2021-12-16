package com.chooloo.www.koler.ui.list

import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.adapter.ListAdapter
import com.chooloo.www.koler.ui.base.BaseController

abstract class ListController<ItemType, V : ListContract.View<ItemType>>(view: V) :
    BaseController<V>(view),
    ListContract.Controller<ItemType, V> {

    private var _onItemsChangedListener: (List<ItemType>) -> Unit? = {}
    private val _adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            onDataChanged()
        }
    }

    override fun onStart() {
        adapter.apply {
            registerAdapterDataObserver(_adapterDataObserver)
            setOnItemClickListener(this@ListController::onItemClick)
            setOnItemLongClickListener(this@ListController::onItemLongClick)
        }

        view.apply {
            setAdapter(adapter)
            showLoading(true)
            setEmptyIcon(noResultsIconRes)
            setEmptyReason(noResultsTextRes)
        }

        refreshData()
    }

    override fun onStop() {
        super.onStop()
        adapter.unregisterAdapterDataObserver(_adapterDataObserver)
    }

    override fun setOnItemsChangedListener(onItemsChangedListener: (List<ItemType>) -> Unit?) {
        _onItemsChangedListener = onItemsChangedListener
    }

    private fun refreshData() {
        fetchData { items, hasPermissions ->
            view.setEmptyReason(if (hasPermissions) noPermissionsTextRes else noResultsTextRes)
            adapter.items = items
        }
    }

    private fun onDataChanged() {
        view.showLoading(false)
        view.showEmpty(adapter.items.isEmpty())
        _onItemsChangedListener.invoke(adapter.items)
    }


    abstract val noResultsIconRes: Int
    abstract val noResultsTextRes: Int
    abstract val noPermissionsTextRes: Int
    abstract val adapter: ListAdapter<ItemType>

    abstract fun applyFilter(filter: String)
    abstract fun fetchData(callback: (items: List<ItemType>, hasPermissions: Boolean) -> Unit)
}