package com.chooloo.www.koler.ui.contacts

import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListPresenter

class ContactsPresenter<V : ListContract.View<Contact>>(view: V) :
    ListPresenter<Contact, V>(view),
    ListContract.Presenter<Contact, V> {

    override val requiredPermissions
        get() = ContactsContentResolver.REQUIRED_PERMISSIONS

    override val noResultsMessage
        get() = boundComponent.stringInteractor.getString(R.string.error_no_results_contacts)

    override val noPermissionsMessage
        get() = boundComponent.stringInteractor.getString(R.string.error_no_permissions_contacts)


    override fun observeData() {
        boundComponent.contactsProviderLiveData.observe(
            boundComponent.lifecycleOwner,
            this::onDataChanged
        )
    }

    override fun applyFilter(filter: String) {
        boundComponent.contactsProviderLiveData.filter = filter
    }

    override fun onItemClick(item: Contact) {
        view.showItem(item)
    }
}