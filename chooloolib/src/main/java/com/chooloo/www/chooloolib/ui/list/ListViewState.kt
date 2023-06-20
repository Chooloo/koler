package com.chooloo.www.chooloolib.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.permissioned.PermissionedViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.MutableDataLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class ListViewState<ItemType>(
    permissions: PermissionsInteractor
) : PermissionedViewState(permissions) {
    private var _isObserve: Boolean = true
    private var _itemsCollectJob: Job? = null
    private var _itemsFlow: Flow<List<ItemType>>? = null

    private val _items = MutableLiveData<List<ItemType>>()
    private val _filter = MutableLiveData<String?>()
    private val _emptyIcon = MutableLiveData<Int>()
    private val _emptyMessage = MutableLiveData<Int>()
    private val _isEmpty = MutableLiveData(true)
    private val _isLoading = MutableLiveData(true)
    private val _isScrollerVisible = MutableLiveData<Boolean>()
    private val _itemClickedEvent = MutableDataLiveEvent<ItemType>()
    private val _itemLongClickedEvent = MutableDataLiveEvent<ItemType>()

    val items = _items as LiveData<List<ItemType>>
    val filter = _filter as LiveData<String?>
    val emptyIcon = _emptyIcon as LiveData<Int>
    val emptyMessage = _emptyMessage as LiveData<Int>
    val isEmpty = _isEmpty as LiveData<Boolean>
    val isLoading = _isLoading as LiveData<Boolean>
    val isScrollerVisible = _isScrollerVisible as LiveData<Boolean>
    val itemClickedEvent = _itemClickedEvent as DataLiveEvent<ItemType>
    val itemLongClickedEvent = _itemLongClickedEvent as DataLiveEvent<ItemType>

    protected open val noResultsIconRes: Int? = null
    protected open val noResultsTextRes: Int? = null


    override fun attach() {
        _emptyIcon.value = noResultsIconRes
        _emptyMessage.value = noResultsTextRes
        updateItemsFlow()
    }

    protected fun updateItemsFlow() {
        _itemsCollectJob?.cancel()
        if (_isObserve) {
            _itemsCollectJob = viewModelScope.launch {
                _itemsFlow = getItemsFlow(filter.value)
                _itemsFlow?.collect(this@ListViewState::onItemsChanged)
            }
        }
    }

    open fun onItemsChanged(items: List<ItemType>) {
        _isLoading.value = false
        _isEmpty.value = items.isEmpty()
        _items.value = items
    }

    open fun onFilterChanged(filter: String?) {
        _filter.value = filter
        updateItemsFlow()
    }

    open fun onItemClick(item: ItemType) {
        _itemClickedEvent.call(item)
    }

    open fun onItemLongClick(item: ItemType) {
        _itemLongClickedEvent.call(item)
    }

    open fun onIsObserve(isObserve: Boolean) {
        _isObserve = isObserve
    }

    open fun onItemLeftClick(item: ItemType) {}
    open fun onItemRightClick(item: ItemType) {}

    abstract fun getItemsFlow(filter: String?): Flow<List<ItemType>>?
}