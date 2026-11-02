package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import com.chooloo.www.chooloolib.ui.caller.CallerTarget
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {
    enum class Tab { RECENTS, CONTACTS }

    data class UiState(
        val tab: Tab? = null,
        val isDialpadOpen: Boolean = false,
        val dialNumber: String? = null,
        val isSearching: Boolean = false,
        val searchText: String = "",
        val selectedCaller: CallerTarget? = null,
        val isSettingsOpen: Boolean = false,
    )

    private data class TransientState(
        val dialNumber: String? = null,
        val selectedCaller: CallerTarget? = null,
        val isSettingsOpen: Boolean = false,
    )

    private val tab = savedStateHandle.getStateFlow<Tab?>(KEY_TAB, null)
    private val isDialpadOpen = savedStateHandle.getStateFlow(KEY_DIALPAD_OPEN, false)
    private val isSearching = savedStateHandle.getStateFlow(KEY_SEARCHING, false)
    private val searchText = savedStateHandle.getStateFlow(KEY_SEARCH_TEXT, "")
    private val transientState = MutableStateFlow(TransientState())

    val uiState: StateFlow<UiState> = combine(tab, isDialpadOpen, isSearching, searchText, transientState) {
            tab, isDialpadOpen, isSearching, searchText, transient ->
        UiState(tab, isDialpadOpen, transient.dialNumber, isSearching, searchText, transient.selectedCaller, transient.isSettingsOpen)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    init {
        viewModelScope.launch {
            if (tab.value != null) return@launch
            val defaultPage = preferenceRepository.settings.first().defaultPage
            if (tab.value != null) return@launch
            savedStateHandle[KEY_TAB] = if (defaultPage == Page.CONTACTS) Tab.CONTACTS else Tab.RECENTS
            if (defaultPage == Page.DIALER) savedStateHandle[KEY_DIALPAD_OPEN] = true
        }
    }

    fun onViewIntent(intent: Intent) {
        if (intent.action !in arrayOf(Intent.ACTION_DIAL, Intent.ACTION_VIEW)) return
        val number = intent.data?.schemeSpecificPart?.let(Uri::decode)?.trim()
        if (number.isNullOrEmpty()) return
        transientState.update { it.copy(dialNumber = number) }
        if (tab.value == null) savedStateHandle[KEY_TAB] = Tab.RECENTS
        savedStateHandle[KEY_DIALPAD_OPEN] = true
    }

    fun onTabSelected(tab: Tab) {
        savedStateHandle[KEY_TAB] = tab
    }

    fun onDialpadOpen() {
        savedStateHandle[KEY_DIALPAD_OPEN] = true
    }

    fun onDialpadClose() {
        savedStateHandle[KEY_DIALPAD_OPEN] = false
        transientState.update { it.copy(dialNumber = null) }
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

    fun onCallerSelected(target: CallerTarget) {
        transientState.update { it.copy(selectedCaller = target) }
    }

    fun onCallerDismiss() {
        transientState.update { it.copy(selectedCaller = null) }
    }

    fun onSettingsOpen() {
        transientState.update { it.copy(isSettingsOpen = true) }
    }

    fun onSettingsClose() {
        transientState.update { it.copy(isSettingsOpen = false) }
    }

    private companion object {
        const val KEY_TAB = "tab"
        const val KEY_DIALPAD_OPEN = "dialpad_open"
        const val KEY_SEARCHING = "searching"
        const val KEY_SEARCH_TEXT = "search_text"
    }
}
