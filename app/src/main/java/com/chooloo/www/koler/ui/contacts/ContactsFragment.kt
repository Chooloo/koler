package com.chooloo.www.koler.ui.contacts

import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.adapter.ContactsAdapter
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.livedata.ContactsProviderLiveData
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.contact.ContactFragment
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
        setOnContactItemClick(_presenter::onContactItemClick)
        setOnContactItemLongClickListener(_presenter::onContactItemLongClick)
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
            number.observe(viewLifecycleOwner, _contactsLiveData::setFilter)
            text.observe(viewLifecycleOwner, _contactsLiveData::setFilter)
        }
        showNoPermissions(false)
    }, blockedCallback = { _presenter.onPermissionsBlocked() })

    override fun openContact(contact: Contact) {
        BottomFragment.newInstance(ContactFragment.newInstance(contact.id)).apply {
            show(_activity.supportFragmentManager, ContactFragment.TAG)
        }
    }
}