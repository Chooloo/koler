package com.chooloo.www.chooloolib.ui.list

import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.chooloolib.adapter.ListAdapter
import com.chooloo.www.chooloolib.ui.base.BaseController

abstract class ListController<ItemType, V : ListContract.View<ItemType>>(
    view: V,
    override val adapter: ListAdapter<ItemType>
) :
    BaseController<V>(view),
    ListContract.Controller<ItemType, V> {

    private var _onItemClickListener: (ItemType) -> Unit = {}
    private var _onItemLongClickListener: (ItemType) -> Unit = {}
    private var _onItemsChangedListener: (List<ItemType>) -> Unit? = {}
    private val _adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            onDataChanged()
        }
    }

    override fun onSetup() {
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

    override fun onItemClick(item: ItemType) {
        super.onItemClick(item)
        _onItemClickListener.invoke(item)
    }

    override fun onItemLongClick(item: ItemType) {
        super.onItemLongClick(item)
        _onItemLongClickListener.invoke(item)
    }

    override fun setOnItemClickListener(onItemClickListener: (ItemType) -> Unit) {
        _onItemClickListener = onItemClickListener
    }

    override fun setOnItemLongClickListener(onItemLongClickListener: (ItemType) -> Unit) {
        _onItemLongClickListener = onItemLongClickListener
    }

    override fun setOnItemsChangedListener(onItemsChangedListener: (List<ItemType>) -> Unit?) {
        _onItemsChangedListener = onItemsChangedListener
    }

    protected fun refreshData() {
        fetchData { items, hasPermissions ->
            view.setEmptyReason(if (hasPermissions) noResultsTextRes else noPermissionsTextRes)
            adapter.items = items
        }
    }

    private fun onDataChanged() {
        view.showLoading(false)
        view.showEmpty(adapter.items.isEmpty())
        _onItemsChangedListener.invoke(adapter.items)
    }

    protected open val noResultsIconRes: Int? = null
    protected open val noResultsTextRes: Int? = null
    protected open val noPermissionsTextRes: Int? = null

    override fun applyFilter(filter: String) {
        try {
            adapter.titleFilter = filter
        } catch (e: NullPointerException) {
        }
    }

    abstract fun fetchData(callback: (items: List<ItemType>, hasPermissions: Boolean) -> Unit)
}