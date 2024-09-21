package com.chooloo.www.koler.viewmodel.main

import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.model.PhoneData
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.domain.repository.clipboard.ClipboardRepository
import com.chooloo.www.chooloolib.domain.repository.telecom.TelecomRepository
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModelImpl
import com.chooloo.www.koler.viewmodel.main.MainViewModel.Companion.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModelImpl @Inject constructor(
    private val telecomRepository: TelecomRepository,
    private val clipboardRepository: ClipboardRepository
) : BaseViewModelImpl(), MainViewModel {
    private val _uiState = MutableStateFlow(UiState())

    override val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    override fun onDismissMenu() {
        _uiState.update { it.copy(isMenuVisible = false) }
    }

    override fun onDismissDialer() {
        _uiState.update { it.copy(isDialerVisible = false) }
    }

    override fun onMenuButtonClick() {
        _uiState.update { it.copy(isMenuVisible = true) }
    }

    override fun onDialerButtonClick() {
        _uiState.update { it.copy(isDialerVisible = true) }
    }

    override fun onDismissSelectedRecentData() {
        _uiState.update { it.copy(selectedRecentData = null) }
    }

    override fun onDismissSelectedContactData() {
        _uiState.update { it.copy(selectedContactData = null) }
    }

    override fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText) }
    }

    override fun onSearchFocusChanged(isFocused: Boolean) {
        _uiState.update { it.copy(isSearchFocused = isFocused) }
    }

    override fun onRecentDataClick(recentData: RecentData) {
        _uiState.update { it.copy(selectedRecentData = recentData) }
    }

    override fun onRecentDataLongClick(recentData: RecentData) {
        clipboardRepository.copyText(recentData.number)
    }

    override fun onContactDataClick(contactData: ContactData) {
        _uiState.update { it.copy(selectedContactData = contactData) }
    }

    override fun onPhoneItemLongClick(phoneItem: PhoneData) {
        clipboardRepository.copyText(phoneItem.number)
    }

    override fun onPhoneItemActionClick(phoneItem: PhoneData) {
        viewModelScope.launch {
            telecomRepository.callNumber(phoneItem.number)
        }
    }
}