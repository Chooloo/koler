package com.chooloo.www.koler.ui.contacts

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.ContactsAdapter
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contact.ContactFragment
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListFragment

class ContactsFragment : ListFragment<Contact, ContactsAdapter>(), ListContract.View<Contact> {
    override val searchHint by lazy { getString(R.string.hint_search_contacts) }
    override lateinit var presenter: ContactsPresenter<ContactsFragment>


    override fun onSetup() {
        presenter = ContactsPresenter(this)
        super.onSetup()
    }

    override fun showItem(item: Contact) {
        BottomFragment(ContactFragment.newInstance(item.id)).show(
            baseActivity.supportFragmentManager,
            ContactFragment.TAG
        )
    }

    fun applyFilter(filter: String) {
        if (this::presenter.isInitialized) {
            presenter.applyFilter(filter)
        }
    }

    companion object {
        fun newInstance(
            isCompact: Boolean = false,
            isSearchable: Boolean = true,
            isHideNoResults: Boolean = false
        ) =
            ContactsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_COMPACT, isCompact)
                    putBoolean(ARG_IS_SEARCHABLE, isSearchable)
                    putBoolean(ARG_IS_HIDE_NO_RESULTS, isHideNoResults)
                }
            }
    }
}