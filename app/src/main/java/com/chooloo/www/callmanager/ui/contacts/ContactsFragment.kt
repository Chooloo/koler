package com.chooloo.www.callmanager.ui.contacts

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.callmanager.adapter.ContactsAdapter
import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.livedata.ContactsLiveData
import com.chooloo.www.callmanager.ui.contact.ContactBottomDialogFragment
import com.chooloo.www.callmanager.ui.list.ListFragment
import com.chooloo.www.callmanager.viewmodel.data.DataViewModel
import com.chooloo.www.callmanager.viewmodel.data.DataViewModelFactory

class ContactsFragment : ListFragment<ContactsAdapter>(), ContactsMvpView {
    private lateinit var _presenter: ContactsMvpPresenter<ContactsMvpView>
    private lateinit var _contactsLiveData: ContactsLiveData

    companion object {
        @JvmStatic
        fun newInstance(): ContactsFragment {
            return ContactsFragment()
        }
    }

    override fun onGetAdapter(): ContactsAdapter {
        return ContactsAdapter(_activity).apply {
            setOnContactItemClick(object : ContactsAdapter.OnContactItemClickListener {
                override fun onContactItemClick(contact: Contact) {
                    _presenter.onContactItemClick(contact)
                }
            })
            setOnContactItemLongClickListener(object : ContactsAdapter.OnContactItemLongClickListener {
                override fun onContactItemLongClick(contact: Contact): Boolean {
                    _presenter.onContactItemLongClick(contact)
                    return true
                }
            })
        }
    }

    override fun onSetup() {
        super.onSetup()

        _presenter = ContactsPresenter()
        _presenter.attach(this)

        _contactsLiveData = ViewModelProvider(this, DataViewModelFactory(_activity)).get(DataViewModel::class.java).contacts
        _contactsLiveData.observe(viewLifecycleOwner, Observer { cursor -> adapter.updateContacts(cursor) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun openContact(contact: Contact) {
        ContactBottomDialogFragment.newInstance(contact).show(_activity.supportFragmentManager, ContactBottomDialogFragment.TAG)
    }
}