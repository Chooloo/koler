package com.chooloo.www.koler.ui.main

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.model.PhoneData
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.domain.repository.clipboard.ClipboardRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository
import com.chooloo.www.chooloolib.domain.repository.preference.PreferenceRepository.Companion.Page
import com.chooloo.www.chooloolib.domain.repository.telecom.TelecomRepository
import com.chooloo.www.chooloolib.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URLDecoder
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val telecomRepository: TelecomRepository,
    private val clipboardRepository: ClipboardRepository,
    preferenceRepository: PreferenceRepository
) : BaseViewModel() {
    data class UiState(
        val searchText: String = "",
        val dialNumber: String? = null,
        val initialPage: Int = Page.CONTACTS.index,
        val isMenuVisible: Boolean = false,
        val isSearchFocused: Boolean = false,
        val selectedRecentData: RecentData? = null,
        val selectedContactData: ContactData? = null,
    )

    private val _uiState = MutableStateFlow(
        UiState(initialPage = preferenceRepository.defaultPage.value.index)
    )

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onViewIntent(intent: Intent) {
        if (intent.action in arrayOf(Intent.ACTION_DIAL, Intent.ACTION_VIEW)) {
            val number = intent.data?.schemeSpecificPart?.let {
                try {
                    URLDecoder.decode(it, "UTF-8")
                } catch (_: Exception) {
                    it
                }
            }?.trim()
            if (!number.isNullOrEmpty()) {
                _uiState.update {
                    it.copy(
                        dialNumber = number,
                        initialPage = Page.DIALER.index
                    )
                }
            }
        }
    }

    fun onDismissMenu() {
        _uiState.update { it.copy(isMenuVisible = false) }
    }

    fun onMenuButtonClick() {
        _uiState.update { it.copy(isMenuVisible = true) }
    }

    fun onDismissSelectedRecentData() {
        _uiState.update { it.copy(selectedRecentData = null) }
    }

    fun onDismissSelectedContactData() {
        _uiState.update { it.copy(selectedContactData = null) }
    }

    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText) }
    }

    fun onSearchFocusChanged(isFocused: Boolean) {
        _uiState.update { it.copy(isSearchFocused = isFocused) }
    }

    fun onRecentDataClick(recentData: RecentData) {
        _uiState.update { it.copy(selectedRecentData = recentData) }
    }

    fun onRecentDataLongClick(recentData: RecentData) {
        clipboardRepository.copyText(recentData.number)
    }

    fun onContactDataClick(contactData: ContactData) {
        _uiState.update { it.copy(selectedContactData = contactData) }
    }

    fun onPhoneItemLongClick(phoneItem: PhoneData) {
        clipboardRepository.copyText(phoneItem.number)
    }

    fun onPhoneItemActionClick(phoneItem: PhoneData) {
        viewModelScope.launch {
            telecomRepository.callNumber(phoneItem.number)
        }
    }
}
