package com.chooloo.www.chooloolib.ui.viewmodel.dialpad

import com.chooloo.www.chooloolib.domain.model.ContactData
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel
import kotlinx.coroutines.flow.StateFlow


interface DialpadViewModel : BaseViewModel {
    companion object {
        data class UiState(
            val value: String = "",
            val deleteEnabled: Boolean = false,
            val addContactEnabled: Boolean = false,
            val suggestions: List<ContactData> = listOf()
        )
    }

    val uiState: StateFlow<UiState>

    fun onCallClick()
    fun onTextPasted()
    fun onDeleteClick()
    fun onDeleteLongClick()
    fun onAddContactClick()
    fun onKeyClick(key: String)
    fun onKeyLongClick(key: String)
}