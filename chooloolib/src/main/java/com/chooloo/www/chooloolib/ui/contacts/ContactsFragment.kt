package com.chooloo.www.chooloolib.ui.contacts

import com.chooloo.www.chooloolib.adapter.ContactsAdapter
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.ui.list.ListFragment

open class ContactsFragment :
    ListFragment<ContactAccount, ContactsAdapter>(),
    ContactsContract.View {

    override val controller: ContactsController<out ContactsFragment> by lazy {
        ContactsController(this)
    }

    override fun onSetup() {
        controller.initialize()
        super.onSetup()
    }

    fun applyFilter(filter: String) {
        controller.applyFilter(filter)
    }

    companion object {
        fun newInstance() = ContactsFragment()
    }
}