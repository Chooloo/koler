package com.chooloo.www.chooloolib.ui.recents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.ui.common.ListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RecentsViewModel @Inject constructor(
    recentRepository: RecentRepository,
    preferenceRepository: PreferenceRepository,
) : ViewModel() {
    private val filter = MutableStateFlow("")

    val uiState: StateFlow<ListUiState<Recent>> = filter
        .flatMapLatest { ListUiState.from(recentRepository.recents(it), TAG) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListUiState())

    val isGroupRecentsEnabled: StateFlow<Boolean> = preferenceRepository.settings
        .map { it.isGroupRecentsEnabled }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onFilterChanged(filter: String) {
        this.filter.value = filter
    }

    private companion object {
        const val TAG = "RecentsViewModel"
    }
}
