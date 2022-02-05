package com.chooloo.www.chooloolib.ui.contacts

import androidx.lifecycle.LifecycleOwner
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.adapter.ContactsAdapter
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.di.factory.livedata.LiveDataFactory
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.list.ListController
import javax.inject.Inject

open class ContactsController @Inject constructor(
    view: ContactsContract.View,
    contactsAdapter: ContactsAdapter,
    private val lifecycleOwner: LifecycleOwner,
    private val liveDataFactory: LiveDataFactory,
    private val permissionsInteractor: PermissionsInteractor,
) :
    ListController<ContactAccount, ContactsContract.View>(view, contactsAdapter),
    ContactsContract.Controller {

    override val noResultsIconRes = R.drawable.round_people_24
    override val noResultsTextRes = R.string.error_no_results_contacts
    override val noPermissionsTextRes = R.string.error_no_permissions_contacts


    private val contactsLiveData by lazy { liveDataFactory.allocContactsProviderLiveData() }

    override fun applyFilter(filter: String) {
        super.applyFilter(filter)
        try {
            contactsLiveData.filter = filter
        } catch (e: Exception) {
        }
    }

    override fun fetchData(callback: (List<ContactAccount>, hasPermissions: Boolean) -> Unit) {
        permissionsInteractor.runWithReadContactsPermissions {
            if (it) {
                contactsLiveData.observe(lifecycleOwner) { data ->
                    callback.invoke(data, true)
                }
            } else {
                callback.invoke(emptyList(), false)
            }
        }
    }
}