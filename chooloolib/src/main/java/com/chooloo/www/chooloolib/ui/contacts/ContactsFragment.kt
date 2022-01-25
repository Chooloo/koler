package com.chooloo.www.chooloolib.ui.contacts

import com.chooloo.www.chooloolib.adapter.ContactsAdapter
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.ui.list.ListFragment
import javax.inject.Inject

open class ContactsFragment :
    ListFragment<ContactAccount, ContactsAdapter>(),
    ContactsContract.View {

    @Inject override lateinit var controller: ContactsContract.Controller<ContactsFragment>


    fun applyFilter(filter: String) {
        controller.applyFilter(filter)
    }

    companion object {
        fun newInstance() = ContactsFragment()
    }
}