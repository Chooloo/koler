package com.chooloo.www.chooloolib.ui.contacts

import android.Manifest.permission.READ_CONTACTS
import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.livedata.ContactsLiveData
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.repository.contacts.ContactsRepository
import com.chooloo.www.chooloolib.ui.list.ListViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class ContactsViewState @Inject constructor(
    contactsRepository: ContactsRepository,
    private val permissions: PermissionsInteractor,
) :
    ListViewState<ContactAccount>() {

    override val noResultsIconRes = R.drawable.round_people_24
    override val noResultsTextRes = R.string.error_no_results_contacts
    override val noPermissionsTextRes = R.string.error_no_permissions_contacts

    private val contactsLiveData = contactsRepository.getContacts() as ContactsLiveData

    val showContactEvent = DataLiveEvent<Long>()


    override fun onFilterChanged(filter: String?) {
        super.onFilterChanged(filter)
        if (permissions.hasSelfPermission(READ_CONTACTS)) {
            onPermissionsChanged(true)
            contactsLiveData.filter = filter
        }
    }

    override fun onItemClick(item: ContactAccount) {
        super.onItemClick(item)
        showContactEvent.call(item.id)
    }

    override fun getItemsObservable(callback: (LiveData<List<ContactAccount>>) -> Unit) {
        permissions.runWithReadContactsPermissions {
            onPermissionsChanged(it)
            if (it) callback.invoke(contactsLiveData)
        }
    }
}