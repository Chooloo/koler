package com.chooloo.www.chooloolib.ui.viewmodel.list

import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import com.chooloo.www.chooloolib.utils.LoadingState
import kotlinx.coroutines.flow.StateFlow

interface RecordsListViewModel<ItemType, RecordType> : BaseViewModel {
    companion object {
        data class UiState<ItemType>(
            val filter: String? = null,
            val items: List<ItemType> = emptyList(),
            val loadingState: LoadingState = LoadingState.IDLE
        )
    }

    val uiState: StateFlow<UiState<ItemType>>

    suspend fun enrichItem(item: ItemType) {}
    suspend fun onFilterChanged(filter: String?)
    suspend fun convertRecordToItem(record: RecordType): ItemType
}