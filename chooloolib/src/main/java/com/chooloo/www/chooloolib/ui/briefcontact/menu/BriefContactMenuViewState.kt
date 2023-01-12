package com.chooloo.www.chooloolib.ui.briefcontact.menu

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.base.menu.BaseMenuViewState
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.chooloolib.util.MutableLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BriefContactMenuViewState @Inject constructor(
    private val contacts: ContactsInteractor,
    private val permissions: PermissionsInteractor
) : BaseMenuViewState() {
    override val menuResList = listOf(R.menu.menu_brief_contact_extra)

    private val _contactId = MutableLiveData<Long>()
    private val _contactName = MutableLiveData<String>()
    private val _isFavorite = MutableLiveData<Boolean>()
    private val _showHistoryEvent = MutableLiveEvent()
    private val _confirmDeleteEvent = MutableLiveEvent()

    val contactId = _contactId as LiveData<Long>
    val isFavorite = _isFavorite as LiveData<Boolean>
    val contactName = _contactName as LiveData<String>
    val showHistoryEvent = _showHistoryEvent as LiveEvent
    val confirmDeleteEvent = _confirmDeleteEvent as LiveEvent


    override fun onMenuItemClick(itemId: Int) {
        when (itemId) {
            R.id.menu_brief_contact_show_history -> onShowHistory()
            R.id.menu_brief_contact_delete -> _confirmDeleteEvent.call()
            R.id.menu_brief_contact_set_favorite -> onSetFavorite(true)
            R.id.menu_brief_contact_unset_favorite -> onSetFavorite(false)
        }
    }

    fun onDelete() {
        permissions.runWithPermissions(arrayOf(Manifest.permission.WRITE_CONTACTS), {
            contactId.value?.let(contacts::deleteContact)
            onFinish()
        }, {
            onError(R.string.error_no_permissions_edit_contacts)
        })
    }

    fun onShowHistory() {
        _showHistoryEvent.call()
    }

    fun onSetFavorite(isFavorite: Boolean) {
        contactId.value?.let { contacts.toggleContactFavorite(it, isFavorite) }
        onFinish()
    }

    fun onIsFavorite(isFavorite: Boolean) {
        _isFavorite.value = isFavorite
    }

    fun onContactId(contactId: Long) {
        _contactId.value = contactId
    }

    fun onContactName(contactName: String) {
        _contactName.value = contactName
    }
}