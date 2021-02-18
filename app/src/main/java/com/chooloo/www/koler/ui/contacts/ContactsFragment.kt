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

    private lateinit var _searchViewModel: SearchViewModel
    private lateinit var _contactsLiveData: ContactsProviderLiveData
    private lateinit var _presenter: ContactsMvpPresenter<ContactsMvpView>

    companion object {
        fun newInstance() = ContactsFragment()
    }

    override fun onGetAdapter() = ContactsAdapter().apply {
        setOnContactItemClick { contact -> _presenter.onContactItemClick(contact) }
        setOnContactItemLongClickListener { contact -> _presenter.onContactItemLongClick(contact) }
    }

    override fun onSetup() {
        super.onSetup()

        _presenter = ContactsPresenter()
        _presenter.attach(this)

        _contactsLiveData = ContactsProviderLiveData(_activity)

        _searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java).apply {
            number.observe(viewLifecycleOwner) { _contactsLiveData.setFilter(it) }
            text.observe(viewLifecycleOwner) { _contactsLiveData.setFilter(it) }
        }

        showEmptyPage(false)
        showNoPermissions(false)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun observe() = runWithPermissions(_contactsLiveData.requiredPermissions) {
        _contactsLiveData.observe(viewLifecycleOwner, listAdapter::updateContacts)
    }

    override fun openContact(contact: Contact) {
        ContactBottomDialogFragment.newInstance(contact.id).show(_activity.supportFragmentManager, ContactBottomDialogFragment.TAG)
    }
}