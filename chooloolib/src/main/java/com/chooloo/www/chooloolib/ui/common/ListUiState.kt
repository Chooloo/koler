package com.chooloo.www.chooloolib.ui.common

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

data class ListUiState<T>(
    val items: List<T> = emptyList(),
    val loadingState: LoadingState = LoadingState.LOADING,
) {
    companion object {
        fun <T> from(items: Flow<List<T>>, tag: String): Flow<ListUiState<T>> = items
            .map { ListUiState(it, LoadingState.SUCCESS) }
            .catch { error ->
                Log.e(tag, "Failed loading items", error)
                emit(ListUiState(loadingState = LoadingState.FAILED))
            }
    }
}
