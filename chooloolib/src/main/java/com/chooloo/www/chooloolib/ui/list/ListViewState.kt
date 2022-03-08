package com.chooloo.www.chooloolib.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent

abstract class ListViewState<ItemType> : BaseViewState() {
    val filter = MutableLiveData<String>()
    val emptyIcon = MutableLiveData<Int>()
    val emptyMessage = MutableLiveData<Int>()
    val isEmpty = MutableLiveData(true)
    val isLoading = MutableLiveData(true)
    val isScrollerVisible = MutableLiveData<Boolean>()

    val itemClickedEvent = DataLiveEvent<ItemType>()
    val itemLongClickedEvent = DataLiveEvent<ItemType>()
    val itemsChangedEvent = DataLiveEvent<List<ItemType>>()


    override fun attach() {
        emptyIcon.value = noResultsIconRes
        emptyMessage.value = noResultsTextRes
    }

    fun onItemsChanged(items: List<ItemType>) {
        isLoading.value = false
        isEmpty.value = items.isEmpty()
        itemsChangedEvent.call(items)
    }

    open fun onFilterChanged(filter: String?) {
        this.filter.value = filter
    }

    open fun onItemClick(item: ItemType) {
        itemClickedEvent.call(item)
    }

    open fun onItemLongClick(item: ItemType) {
        itemLongClickedEvent.call(item)
    }

    fun onPermissionsChanged(hasPermissions: Boolean) {
        emptyMessage.value = if (hasPermissions) noResultsTextRes else noPermissionsTextRes
    }
    open fun onItemLeftClick(item: ItemType) {}
    open fun onItemRightClick(item: ItemType) {}

    protected open val noResultsIconRes: Int? = null
    protected open val noResultsTextRes: Int? = null
    protected open val noPermissionsTextRes: Int? = null
    abstract fun getItemsObservable(callback: (LiveData<List<ItemType>>) -> Unit)
}