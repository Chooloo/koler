package com.chooloo.www.chooloolib.ui.recents

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.ui.common.ListUiState
import com.chooloo.www.chooloolib.ui.common.LoadingState
import com.chooloo.www.chooloolib.ui.common.UserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RecentsViewModel @Inject constructor(
    private val recentRepository: RecentRepository,
    preferenceRepository: PreferenceRepository,
) : ViewModel() {
    data class UiState(
        val items: List<Recent> = emptyList(),
        val loadingState: LoadingState = LoadingState.LOADING,
        val isGroupRecentsEnabled: Boolean = false,
        val message: UserMessage? = null,
    )

    private val filter = MutableStateFlow("")
    private val message = MutableStateFlow<UserMessage?>(null)

    val uiState: StateFlow<UiState> = combine(
        filter.flatMapLatest { ListUiState.from(recentRepository.recents(it), TAG) },
        preferenceRepository.settings.map { it.isGroupRecentsEnabled },
        message,
    ) { list, isGroupRecentsEnabled, message ->
        UiState(list.items, list.loadingState, isGroupRecentsEnabled, message)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun onFilterChanged(filter: String) {
        this.filter.value = filter
    }

    fun onDeleteConfirmed(recentIds: List<Long>) {
        viewModelScope.launch {
            try {
                recentRepository.deleteRecents(recentIds)
            } catch (error: SecurityException) {
                onDeleteFailed(error)
            } catch (error: IllegalArgumentException) {
                onDeleteFailed(error)
            } catch (error: SQLiteException) {
                onDeleteFailed(error)
            }
        }
    }

    fun onMessageShown() {
        message.value = null
    }

    private fun onDeleteFailed(error: Exception) {
        Log.e(TAG, "Failed deleting recents", error)
        message.value = UserMessage.DELETE_RECENTS_FAILED
    }

    private companion object {
        const val TAG = "RecentsViewModel"
    }
}
