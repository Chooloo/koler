package com.chooloo.www.koler.viewmodel.main

import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.domain.model.PhoneData
import com.chooloo.www.chooloolib.domain.model.RecentData
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.StateFlow

interface MainViewModel : BaseViewModel {
    companion object {
        data class UiState(
            val searchText: String = "",
            val isMenuVisible: Boolean = false,
            val isDialerVisible: Boolean = false,
            val isSearchFocused: Boolean = false,
            val selectedRecentData: RecentData? = null,
            val selectedContactData: ContactData? = null,
        )
    }

    val uiState: StateFlow<UiState>

    fun onDismissMenu()
    fun onDismissDialer()
    fun onMenuButtonClick()
    fun onDialerButtonClick()
    fun onDismissSelectedRecentData()
    fun onDismissSelectedContactData()
    fun onSearchTextChange(searchText: String)
    fun onSearchFocusChanged(isFocused: Boolean)
    fun onRecentDataClick(recentData: RecentData)
    fun onRecentDataLongClick(recentData: RecentData)
    fun onContactDataClick(contactData: ContactData)
    fun onPhoneItemLongClick(phoneItem: PhoneData)
    fun onPhoneItemActionClick(phoneItem: PhoneData)
}