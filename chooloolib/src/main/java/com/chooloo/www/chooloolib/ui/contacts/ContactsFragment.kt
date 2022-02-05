package com.chooloo.www.chooloolib.ui.contacts

import com.chooloo.www.chooloolib.adapter.ContactsAdapter
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class ContactsFragment @Inject constructor() :
    ListFragment<ContactAccount, ContactsAdapter>(),
    ContactsContract.View {

    override lateinit var controller: ContactsContract.Controller


    override fun onSetup() {
        controller = controllerFactory.getContactsController(this)
        super.onSetup()
    }

    fun applyFilter(filter: String) {
        controller.applyFilter(filter)
    }
}