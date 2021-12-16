package com.chooloo.www.koler.ui.contacts

import android.os.Bundle
import com.chooloo.www.koler.adapter.ContactsAdapter
import com.chooloo.www.koler.data.account.ContactAccount
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contact.ContactFragment
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListFragment

class ContactsFragment : ListFragment<ContactAccount, ContactsAdapter>(),
    ListContract.View<ContactAccount> {
    override lateinit var controller: ContactsController<ContactsFragment>


    override fun onSetup() {
        controller = ContactsController(this)
        super.onSetup()
    }

    override fun showItem(item: ContactAccount) {
        BottomFragment(ContactFragment.newInstance(item.id)).show(
            baseActivity.supportFragmentManager,
            ContactFragment.TAG
        )
    }

    fun applyFilter(filter: String) {
        if (this::controller.isInitialized) {
            controller.applyFilter(filter)
        }
    }

    companion object {
        fun newInstance(
            isHideNoResults: Boolean = false
        ) = ContactsFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_IS_HIDE_NO_RESULTS, isHideNoResults)
            }
        }
    }
}