package com.chooloo.www.kontacts.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.ui.caller.CallerTarget
import com.chooloo.www.chooloolib.ui.home.HomeSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    data class UiState(
        val isSearching: Boolean = false,
        val searchText: String = "",
        val selectedCaller: CallerTarget? = null,
        val isSettingsOpen: Boolean = false,
    )

    val search = HomeSearch(savedStateHandle)

    private val selectedCaller = MutableStateFlow<CallerTarget?>(null)
    private val isSettingsOpen = MutableStateFlow(false)

    val uiState: StateFlow<UiState> = combine(
        search.isOpen,
        search.text,
        selectedCaller,
        isSettingsOpen,
        ::UiState,
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun onContactLinkOpened(link: String) {
        selectedCaller.value = CallerTarget.LinkTarget(link)
    }

    fun onContactSelected(contactId: Long) {
        selectedCaller.value = CallerTarget.ContactTarget(contactId)
    }

    fun onContactDismiss() {
        selectedCaller.value = null
    }

    fun onSettingsOpen() {
        isSettingsOpen.value = true
    }

    fun onSettingsClose() {
        isSettingsOpen.value = false
    }
}
