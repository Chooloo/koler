package com.chooloo.www.koler.ui.contacts

import com.chooloo.www.koler.adapter.ContactsAdapter
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.livedata.ContactsProviderLiveData
import com.chooloo.www.koler.ui.contact.ContactBottomDialogFragment
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.util.runWithPermissions

class ContactsFragment : ListFragment<ContactsAdapter>(), ContactsMvpView {

    private lateinit var _presenter: ContactsMvpPresenter<ContactsMvpView>
    private lateinit var _contactsLiveData: ContactsProviderLiveData

    companion object {
        fun newInstance(): ContactsFragment = ContactsFragment()
    }

    override fun onGetAdapter(): ContactsAdapter {
        return ContactsAdapter(_activity).apply {
            setOnContactItemClick { contact -> _presenter.onContactItemClick(contact) }
            setOnContactItemLongClickListener { contact -> _presenter.onContactItemLongClick(contact) }
        }
    }

    override fun onSetup() {
        super.onSetup()

        _presenter = ContactsPresenter()
        _presenter.attach(this)

        _contactsLiveData = ContactsProviderLiveData(_activity)

        showEmptyPage(false)
        showNoPermissions(false)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun observe() = runWithPermissions(ContactsProviderLiveData.REQUIRED_PERMISSIONS) {
        _contactsLiveData.observe(viewLifecycleOwner, { contacts -> adapter.updateContacts(contacts) })
    }

    override fun openContact(contact: Contact) {
        ContactBottomDialogFragment.newInstance(contact).show(_activity.supportFragmentManager, ContactBottomDialogFragment.TAG)
    }
}