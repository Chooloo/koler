package com.chooloo.www.chooloolib.ui.viewmodel.list

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModelImpl
import com.chooloo.www.chooloolib.ui.viewmodel.list.RecordsListViewModel.Companion.UiState
import com.chooloo.www.chooloolib.utils.LoadingState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class RecordsListViewModelImpl<ItemType, RecordType> :
    BaseViewModelImpl(),
    RecordsListViewModel<ItemType, RecordType> {
    private var _itemsCollectJob: Job? = null
    private var _itemsFlow: Flow<List<RecordType>>? = null

    private val _uiState = MutableStateFlow<UiState<ItemType>>(UiState())

    override val uiState: StateFlow<UiState<ItemType>> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(loadingState = LoadingState.SUCCESS)
        }
    }

    fun updateItemsFlow() {
        _uiState.update {
            it.copy(loadingState = LoadingState.LOADING)
        }
        _uiState.update {
            _itemsCollectJob?.cancel()
            _itemsCollectJob = viewModelScope.launch {
                _itemsFlow = getRecordsFlow(it.filter)
                _itemsFlow?.collect(::onRecordsChanged)
            }
            it
        }
    }

    private fun onRecordsChanged(records: List<RecordType>) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingState = LoadingState.SUCCESS,
                    items = records.map { record -> convertRecordToItem(record) }
                )
            }
        }

//        viewModelScope.launch {
//            _uiState.update {
//                it.copy(items = it.items.map { enrichItem(it) })
//            }
//        }
    }

    override suspend fun onFilterChanged(filter: String?) {
        _uiState.update { it.copy(filter = filter) }
        updateItemsFlow()
    }

    override suspend fun enrichItem(item: ItemType): ItemType = item

    abstract override suspend fun convertRecordToItem(record: RecordType): ItemType

    abstract fun getRecordsFlow(filter: String?): Flow<List<RecordType>>?

}