package com.chooloo.www.chooloolib.ui.common

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.utils.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class RecordsListViewModel<ItemType, RecordType> : BaseViewModel() {
    data class UiState<ItemType>(
        val filter: String? = null,
        val items: List<ItemType> = emptyList(),
        val loadingState: LoadingState = LoadingState.IDLE
    )

    private var _itemsCollectJob: Job? = null
    private var _itemsFlow: Flow<List<RecordType>>? = null

    protected val _uiState = MutableStateFlow<UiState<ItemType>>(
        UiState(loadingState = LoadingState.SUCCESS)
    )

    val uiState: StateFlow<UiState<ItemType>> = _uiState.asStateFlow()

    fun updateItemsFlow() {
        _uiState.update { it.copy(loadingState = LoadingState.LOADING) }
        _itemsCollectJob?.cancel()
        _itemsCollectJob = viewModelScope.launch {
            _itemsFlow = getRecordsFlow(_uiState.value.filter)
            val flow = _itemsFlow
            if (flow == null) {
                _uiState.update { it.copy(items = emptyList(), loadingState = LoadingState.SUCCESS) }
            } else {
                flow.collect(::onRecordsChanged)
            }
        }
    }

    private fun onRecordsChanged(records: List<RecordType>) {
        viewModelScope.launch {
            val mappedItems = withContext(Dispatchers.IO) {
                records.map { record -> enrichItem(convertRecordToItem(record)) }
            }
            _uiState.update {
                it.copy(
                    loadingState = LoadingState.SUCCESS,
                    items = mappedItems
                )
            }
        }
    }

    fun onFilterChanged(filter: String?) {
        if (_uiState.value.filter == filter && _itemsCollectJob != null) return
        _uiState.update { it.copy(filter = filter) }
        updateItemsFlow()
    }

    open suspend fun enrichItem(item: ItemType): ItemType = item

    abstract suspend fun convertRecordToItem(record: RecordType): ItemType

    abstract fun getRecordsFlow(filter: String?): Flow<List<RecordType>>?
}
