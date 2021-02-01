package com.chooloo.www.callmanager.ui.contacts

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.callmanager.adapter.ContactsAdapter
import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.livedata.ContactsLiveData
import com.chooloo.www.callmanager.ui.contact.ContactBottomDialogFragment
import com.chooloo.www.callmanager.ui.cursor.CursorFragment
import com.chooloo.www.callmanager.viewmodel.CursorsViewModel
import com.chooloo.www.callmanager.viewmodelfactory.CursorsViewModelFactory

class ContactsFragment : CursorFragment<ContactsAdapter>(), ContactsMvpView {
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
            setOnContactItemClick { contact: Contact -> _presenter.onContactItemClick(contact) }
            setOnContactItemLongClickListener { contact: Contact -> _presenter.onContactItemLongClick(contact) }
        }
    }

    override fun onSetup() {
        super.onSetup()

        _presenter = ContactsPresenter()
        _presenter.attach(this)

        _contactsLiveData = ViewModelProvider(this, CursorsViewModelFactory(_activity)).get(CursorsViewModel::class.java).contacts
        _contactsLiveData.observe(viewLifecycleOwner, Observer { cursor -> updateData(cursor) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun openContact(contact: Contact) {
        ContactBottomDialogFragment.newInstance(contact).show(_activity.supportFragmentManager, ContactBottomDialogFragment.TAG)
    }
}