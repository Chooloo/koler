package com.chooloo.www.chooloolib.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.repository.ContactRepository
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.ui.common.ListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ContactsViewModel @Inject constructor(
    contactRepository: ContactRepository,
    preferenceRepository: PreferenceRepository,
) : ViewModel() {
    private val filter = MutableStateFlow("")
    private val order = preferenceRepository.settings.map { it.contactOrder }.distinctUntilChanged()

    val uiState: StateFlow<ListUiState<Contact>> = combine(filter, order, ::Pair)
        .flatMapLatest { (filter, order) ->
            ListUiState.from(contactRepository.contacts(filter, order), TAG)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListUiState())

    fun onFilterChanged(filter: String) {
        this.filter.value = filter
    }

    private companion object {
        const val TAG = "ContactsViewModel"
    }
}
