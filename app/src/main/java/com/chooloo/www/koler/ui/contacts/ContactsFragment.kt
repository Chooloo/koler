package com.chooloo.www.koler.ui.contacts

import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.ContactsAdapter
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.livedata.ContactsProviderLiveData
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contact.ContactFragment
import com.chooloo.www.koler.ui.list.ListFragment

class ContactsFragment : ListFragment<Contact, ContactsAdapter>(), ContactsContract.View {
    private var _onContactsChangedListener: (ArrayList<Contact>) -> Unit? = {}
    private val _contactsLiveData by lazy { ContactsProviderLiveData(baseActivity) }
    private val _presenter by lazy { ContactsPresenter<ContactsContract.View>(this) }

    override val adapter by lazy { ContactsAdapter() }
    override val searchHint by lazy { getString(R.string.hint_search_contacts) }
    override val requiredPermissions = ContactsContentResolver.REQUIRED_PERMISSIONS
    override val noResultsMessage by lazy { getString(R.string.error_no_results_contacts) }
    override val noPermissionsMessage by lazy { getString(R.string.error_no_permissions_contacts) }


    //region list fragment

    override fun onAttachData() {
        _contactsLiveData.observe(viewLifecycleOwner) {
            _presenter.onContactsChanged(it)
            _onContactsChangedListener.invoke(it)
        }
    }

    override fun applyFilter(filter: String) {
        _contactsLiveData.filter = filter
    }

    override fun onItemClick(item: Contact) {
        _presenter.onContactItemClick(item)
    }

    override fun onItemLongClick(item: Contact) {
        _presenter.onContactItemLongClick(item)
    }

    //endregion


    //region contacts view

    override fun openContact(contact: Contact) {
        BottomFragment(ContactFragment.newInstance(contact.id)).show(
            baseActivity.supportFragmentManager,
            ContactFragment.TAG
        )
    }

    //endregion


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