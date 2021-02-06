package com.chooloo.www.callmanager.ui.contacts

import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.callmanager.adapter.ContactsAdapter
import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.livedata.ContactsLiveData
import com.chooloo.www.callmanager.ui.contact.ContactBottomDialogFragment
import com.chooloo.www.callmanager.ui.list.ListFragment
import com.chooloo.www.callmanager.util.runWithPermissions
import com.chooloo.www.callmanager.viewmodel.data.DataViewModel
import com.chooloo.www.callmanager.viewmodel.data.DataViewModelFactory

class ContactsFragment : ListFragment<ContactsAdapter>(), ContactsMvpView {

    private lateinit var _presenter: ContactsMvpPresenter<ContactsMvpView>
    private lateinit var _contactsLiveData: ContactsLiveData

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

        _contactsLiveData = ViewModelProvider(this, DataViewModelFactory(_activity)).get(DataViewModel::class.java).contacts
        showEmptyPage(false)
        showNoPermissions(false)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun observe() = runWithPermissions(ContactsLiveData.REQUIRED_PERMISSION, callback = {
        _contactsLiveData.observe(viewLifecycleOwner, { contacts -> adapter.updateContacts(contacts) })
    })

    override fun openContact(contact: Contact) {
        ContactBottomDialogFragment.newInstance(contact).show(_activity.supportFragmentManager, ContactBottomDialogFragment.TAG)
    }
}