package com.chooloo.www.chooloolib.ui.recents

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.Recent
import com.chooloo.www.chooloolib.domain.repository.PhoneRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.RecentRepository
import com.chooloo.www.chooloolib.ui.common.RecordsListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class RecentsViewModel @Inject constructor(
    private val phoneRepository: PhoneRepository,
    private val recentRepository: RecentRepository,
    preferenceRepository: PreferenceRepository,
) : RecordsListViewModel<Recent>() {
    val isGroupRecentsEnabled: StateFlow<Boolean> = preferenceRepository.settings
        .map { it.isGroupRecentsEnabled }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    override suspend fun enrichItem(item: Recent): Recent {
        if (item.number.isEmpty() || item.name != null) return item
        val lookup = phoneRepository.lookup(item.number) ?: return item
        return item.copy(
            name = lookup.name,
            photoUri = lookup.photoUri ?: item.photoUri,
            numberType = lookup.type,
            numberLabel = lookup.label,
        )
    }

    override fun getItemsFlow(filter: String?) = recentRepository.recents(filter)
}
