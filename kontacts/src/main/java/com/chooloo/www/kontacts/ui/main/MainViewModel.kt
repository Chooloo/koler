package com.chooloo.www.kontacts.ui.main

import android.content.ContentUris
import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.repository.contact.ContactRepository
import com.chooloo.www.chooloolib.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val contactRepository: ContactRepository
) : BaseViewModel() {
    data class UiState(
        val searchText: String = "",
        val isMenuVisible: Boolean = false,
        val isSearchFocused: Boolean = false,
        val selectedContactId: Long? = null,
    )

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onViewIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            intent.data?.let { uri ->
                try {
                    val contactId = ContentUris.parseId(uri)
                    _uiState.update { it.copy(selectedContactId = contactId) }
                } catch (_: Exception) {
                }
            }
        }
    }

    fun onMenuButtonClick() {
        _uiState.update { it.copy(isMenuVisible = true) }
    }

    fun onDismissMenu() {
        _uiState.update { it.copy(isMenuVisible = false) }
    }

    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText) }
    }

    fun onSearchFocusChanged(isFocused: Boolean) {
        _uiState.update { it.copy(isSearchFocused = isFocused) }
    }

    fun onContactDataClick(contactData: ContactData) {
        _uiState.update { it.copy(selectedContactId = contactData.id) }
    }

    fun onDismissSelectedContact() {
        _uiState.update { it.copy(selectedContactId = null) }
    }

    fun onAddContactClick() {
        viewModelScope.launch {
            contactRepository.createContact("")
        }
    }
}
