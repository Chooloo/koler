package com.chooloo.www.kontacts.ui.main

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    data class UiState(
        val isSearching: Boolean = false,
        val searchText: String = "",
        val selectedContactId: Long? = null,
        val isSettingsOpen: Boolean = false,
    )

    private val selectedContactId = MutableStateFlow<Long?>(null)
    private val isSettingsOpen = MutableStateFlow(false)

    val uiState: StateFlow<UiState> = combine(
        savedStateHandle.getStateFlow(KEY_SEARCHING, false),
        savedStateHandle.getStateFlow(KEY_SEARCH_TEXT, ""),
        selectedContactId,
        isSettingsOpen,
        ::UiState,
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun onViewIntent(intent: Intent) {
        if (intent.action != Intent.ACTION_VIEW) return
        selectedContactId.value = intent.data?.lastPathSegment?.toLongOrNull() ?: return
    }

    fun onSearchOpen() {
        savedStateHandle[KEY_SEARCHING] = true
    }

    fun onSearchClose() {
        savedStateHandle[KEY_SEARCHING] = false
        savedStateHandle[KEY_SEARCH_TEXT] = ""
    }

    fun onSearchTextChange(text: String) {
        savedStateHandle[KEY_SEARCH_TEXT] = text
    }

    fun onContactSelected(contactId: Long) {
        selectedContactId.value = contactId
    }

    fun onContactDismiss() {
        selectedContactId.value = null
    }

    fun onSettingsOpen() {
        isSettingsOpen.value = true
    }

    fun onSettingsClose() {
        isSettingsOpen.value = false
    }

    private companion object {
        const val KEY_SEARCHING = "searching"
        const val KEY_SEARCH_TEXT = "search_text"
    }
}
