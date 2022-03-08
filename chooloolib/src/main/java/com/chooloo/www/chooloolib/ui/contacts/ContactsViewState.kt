package com.chooloo.www.chooloolib.ui.contacts

import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.livedata.contentprovider.ContactsProviderLiveData
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

    private val contactsLiveData = contactsRepository.getContacts() as ContactsProviderLiveData

    val showContactEvent = DataLiveEvent<Long>()


    override fun onFilterChanged(filter: String?) {
        super.onFilterChanged(filter)
        contactsLiveData.filter = filter
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