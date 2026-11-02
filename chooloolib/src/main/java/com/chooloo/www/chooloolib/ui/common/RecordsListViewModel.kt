package com.chooloo.www.chooloolib.ui.common

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.utils.LoadingState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RecordsListViewModel<ItemType> : BaseViewModel() {
    data class UiState<ItemType>(
        val filter: String? = null,
        val items: List<ItemType> = emptyList(),
        val loadingState: LoadingState = LoadingState.IDLE
    )

    private var _itemsCollectJob: Job? = null

    protected val _uiState = MutableStateFlow<UiState<ItemType>>(UiState(loadingState = LoadingState.SUCCESS))

    val uiState: StateFlow<UiState<ItemType>> = _uiState.asStateFlow()

    fun updateItemsFlow() {
        _uiState.update { it.copy(loadingState = LoadingState.LOADING) }
        _itemsCollectJob?.cancel()
        val flow = getItemsFlow(_uiState.value.filter)
        if (flow == null) {
            _uiState.update { it.copy(items = emptyList(), loadingState = LoadingState.SUCCESS) }
            return
        }
        _itemsCollectJob = viewModelScope.launch {
            flow.mapLatest { items -> items.map { enrichItem(it) } }
                .onEach { items -> _uiState.update { it.copy(items = items, loadingState = LoadingState.SUCCESS) } }
                .catch { error ->
                    Log.e(TAG, "Failed loading items", error)
                    _uiState.update { it.copy(loadingState = LoadingState.FAILED) }
                }
                .collect()
        }
    }

    fun onFilterChanged(filter: String?) {
        if (_uiState.value.filter == filter && _itemsCollectJob != null) return
        _uiState.update { it.copy(filter = filter) }
        updateItemsFlow()
    }

    open suspend fun enrichItem(item: ItemType): ItemType = item

    abstract fun getItemsFlow(filter: String?): Flow<List<ItemType>>?

    private companion object {
        const val TAG = "RecordsListViewModel"
    }
}
