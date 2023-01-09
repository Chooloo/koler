package com.chooloo.www.chooloolib.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.permissioned.PermissionedViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class ListViewState<ItemType>(
    permissions: PermissionsInteractor
) : PermissionedViewState(permissions) {
    private var _itemsFlow: Flow<List<ItemType>>? = null

    val items = MutableLiveData<List<ItemType>>()
    val filter = MutableLiveData<String>()
    val emptyIcon = MutableLiveData<Int>()
    val emptyMessage = MutableLiveData<Int>()
    val isEmpty = MutableLiveData(true)
    val isLoading = MutableLiveData(true)
    val isScrollerVisible = MutableLiveData<Boolean>()

    val itemClickedEvent = DataLiveEvent<ItemType>()
    val itemLongClickedEvent = DataLiveEvent<ItemType>()

    protected open val noResultsIconRes: Int? = null
    protected open val noResultsTextRes: Int? = null


    override fun attach() {
        emptyIcon.value = noResultsIconRes
        emptyMessage.value = noResultsTextRes
        updateItemsFlow()
    }

    protected fun updateItemsFlow() {
        viewModelScope.launch {
            _itemsFlow = getItemsFlow(filter.value)
            _itemsFlow?.collect(this@ListViewState::onItemsChanged)
        }
    }

    open fun onItemsChanged(items: List<ItemType>) {
        isLoading.value = false
        isEmpty.value = items.isEmpty()
        this.items.value = items
    }

    open fun onFilterChanged(filter: String?) {
        this.filter.value = filter
        updateItemsFlow()
    }

    open fun onItemClick(item: ItemType) {
        itemClickedEvent.call(item)
    }

    open fun onItemLongClick(item: ItemType) {
        itemLongClickedEvent.call(item)
    }

    open fun onItemLeftClick(item: ItemType) {}
    open fun onItemRightClick(item: ItemType) {}

    abstract fun getItemsFlow(filter: String?): Flow<List<ItemType>>?
}