package com.chooloo.www.koler.ui.contacts

import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.adapter.ContactsAdapter
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.livedata.ContactsProviderLiveData
import com.chooloo.www.koler.ui.contact.ContactBottomDialogFragment
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.util.permissions.runWithPermissions
import com.chooloo.www.koler.viewmodel.SearchViewModel

class ContactsFragment : ListFragment<ContactsAdapter>(), ContactsMvpView {

    companion object {
        fun newInstance() = ContactsFragment()
    }

    private val _searchViewModel by lazy { ViewModelProvider(requireActivity()).get(SearchViewModel::class.java) }
    private val _contactsLiveData by lazy { ContactsProviderLiveData(_activity) }
    private val _presenter: ContactsMvpPresenter<ContactsMvpView> by lazy { ContactsPresenter() }

    override fun onGetAdapter() = ContactsAdapter().apply {
        setOnContactItemClick { contact -> _presenter.onContactItemClick(contact) }
        setOnContactItemLongClickListener { contact -> _presenter.onContactItemLongClick(contact) }
    }

    override fun onSetup() {
        super.onSetup()

        _presenter.attach(this)

        showEmptyPage(false)
        showNoPermissions(false)
        observe()
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun observe() = runWithPermissions(_contactsLiveData.requiredPermissions, {
        _contactsLiveData.observe(viewLifecycleOwner, listAdapter::updateContacts)
        _searchViewModel.apply {
            number.observe(viewLifecycleOwner) { _contactsLiveData.setFilter(it) }
            text.observe(viewLifecycleOwner) { _contactsLiveData.setFilter(it) }
        }
    }, blockedCallback = { _presenter.onPermissionsBlocked() })

    override fun openContact(contact: Contact) {
        ContactBottomDialogFragment.newInstance(contact.id)
            .show(_activity.supportFragmentManager, ContactBottomDialogFragment.TAG)
    }
}