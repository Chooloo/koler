package com.chooloo.www.chooloolib.ui.briefcontact.menu

import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.base.menu.BaseMenuViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BriefContactMenuViewState @Inject constructor(
    private val contacts: ContactsInteractor,
    private val permissions: PermissionsInteractor
) : BaseMenuViewState() {
    override val menuResList = listOf(R.menu.menu_brief_contact_extra)

    val contactId = MutableLiveData<Long>()
    val contactName = MutableLiveData<String>()
    val isFavorite = MutableLiveData<Boolean>()
    val showHistoryEvent = LiveEvent()

    override fun onMenuItemClick(itemId: Int) {
        when (itemId) {
            R.id.menu_brief_contact_show_history -> onShowHistory()
            R.id.menu_brief_contact_set_favorite -> onSetFavorite(true)
            R.id.menu_brief_contact_unset_favorite -> onSetFavorite(false)
        }
    }

    fun onDelete() {
        permissions.runWithWriteContactsPermissions {
            contactId.value?.let(contacts::deleteContact)
            finishEvent.call()
        }
    }

    fun onShowHistory() {
        showHistoryEvent.call()
    }

    fun onSetFavorite(isFavorite: Boolean) {
        contactId.value?.let { contacts.toggleContactFavorite(it, isFavorite) }
        finishEvent.call()
    }
}