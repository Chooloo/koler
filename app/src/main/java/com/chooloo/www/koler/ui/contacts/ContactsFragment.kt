package com.chooloo.www.koler.ui.contacts

import com.chooloo.www.koler.adapter.ContactsAdapter
import com.chooloo.www.koler.data.account.ContactAccount
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contact.ContactFragment
import com.chooloo.www.koler.ui.list.ListFragment

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

    override fun openContact(contact: ContactAccount) {
        BottomFragment(ContactFragment.newInstance(contact.id)).show(
            baseActivity.supportFragmentManager,
            ContactFragment.TAG
        )
    }

    fun applyFilter(filter: String) {
        controller.applyFilter(filter)
    }

    companion object {
        fun newInstance() = ContactsFragment()
    }
}