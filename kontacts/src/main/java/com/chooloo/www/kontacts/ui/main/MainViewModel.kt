package com.chooloo.www.kontacts.ui.main

import android.content.Intent
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {
    data class UiState(
        val searchText: String = "",
        val isMenuVisible: Boolean = false,
        val selectedContactId: Long? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onViewIntent(intent: Intent) {
        val uri = intent.data?.takeIf { intent.action == Intent.ACTION_VIEW } ?: return
        val contactId = uri.lastPathSegment?.toLongOrNull() ?: return
        _uiState.update { it.copy(selectedContactId = contactId) }
    }

    fun onMenuButtonClick() = _uiState.update { it.copy(isMenuVisible = true) }

    fun onDismissMenu() = _uiState.update { it.copy(isMenuVisible = false) }

    fun onSearchTextChange(searchText: String) = _uiState.update { it.copy(searchText = searchText) }

    fun onContactClick(contact: Contact) = _uiState.update { it.copy(selectedContactId = contact.id) }

    fun onDismissSelectedContact() = _uiState.update { it.copy(selectedContactId = null) }
}
