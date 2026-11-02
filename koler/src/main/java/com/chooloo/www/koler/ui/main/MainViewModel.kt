package com.chooloo.www.koler.ui.main

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.Contact
import com.chooloo.www.chooloolib.domain.model.Page
import com.chooloo.www.chooloolib.domain.model.RecentGroup
import com.chooloo.www.chooloolib.domain.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    preferenceRepository: PreferenceRepository
) : ViewModel() {
    data class UiState(
        val searchText: String = "",
        val dialNumber: String? = null,
        val initialPage: Page? = null,
        val isMenuVisible: Boolean = false,
        val selectedRecentGroup: RecentGroup? = null,
        val selectedContact: Contact? = null,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val defaultPage = preferenceRepository.settings.first().defaultPage
            _uiState.update { it.copy(initialPage = it.initialPage ?: defaultPage) }
        }
    }

    fun onViewIntent(intent: Intent) {
        if (intent.action !in arrayOf(Intent.ACTION_DIAL, Intent.ACTION_VIEW)) return
        val number = intent.data?.schemeSpecificPart?.let(Uri::decode)?.trim()
        if (!number.isNullOrEmpty()) {
            _uiState.update { it.copy(dialNumber = number, initialPage = Page.DIALER) }
        }
    }

    fun onMenuButtonClick() = _uiState.update { it.copy(isMenuVisible = true) }

    fun onDismissMenu() = _uiState.update { it.copy(isMenuVisible = false) }

    fun onSearchTextChange(searchText: String) = _uiState.update { it.copy(searchText = searchText) }

    fun onRecentGroupClick(group: RecentGroup) = _uiState.update { it.copy(selectedRecentGroup = group) }

    fun onDismissSelectedRecentGroup() = _uiState.update { it.copy(selectedRecentGroup = null) }

    fun onContactClick(contact: Contact) = _uiState.update { it.copy(selectedContact = contact) }

    fun onDismissSelectedContact() = _uiState.update { it.copy(selectedContact = null) }
}
