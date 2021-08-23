package com.chooloo.www.koler.ui.contacts

import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.ContactsAdapter
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contact.ContactFragment
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListFragment

class ContactsFragment : ListFragment<Contact, ContactsAdapter>(), ListContract.View<Contact> {
    override val adapter by lazy { ContactsAdapter() }
    override val searchHint by lazy { getString(R.string.hint_search_contacts) }
    override val presenter: ContactsPresenter<ContactsFragment> by lazy { ContactsPresenter(this) }

    private var _onContactsChangedListener: (ArrayList<Contact>) -> Unit? = {}


    override fun updateData(dataList: ArrayList<Contact>) {
        adapter.data = ListBundle.fromContacts(dataList)
        _onContactsChangedListener.invoke(dataList)
    }

    override fun showItem(item: Contact) {
        BottomFragment(ContactFragment.newInstance(item.id)).show(
            baseActivity.supportFragmentManager,
            ContactFragment.TAG
        )
    }

    fun setOnContactsChangedListener(onContactsChangedListener: (ArrayList<Contact>) -> Unit? = {}) {
        _onContactsChangedListener = onContactsChangedListener
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