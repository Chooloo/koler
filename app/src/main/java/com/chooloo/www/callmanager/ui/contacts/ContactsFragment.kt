package com.chooloo.www.callmanager.ui.contacts

import android.Manifest.permission
import android.database.Cursor
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.loader.content.Loader
import com.chooloo.www.callmanager.adapter.ContactsAdapter
import com.chooloo.www.callmanager.cursorloader.FavoritesAndContactsLoader
import com.chooloo.www.callmanager.entity.Contact
import com.chooloo.www.callmanager.livedata.ContactsLiveData
import com.chooloo.www.callmanager.ui.contact.ContactBottomDialogFragment
import com.chooloo.www.callmanager.ui.cursor.CursorFragment
import com.chooloo.www.callmanager.viewmodel.ContactsViewModel

class ContactsFragment : CursorFragment<ContactsAdapter?>(), ContactsMvpView {
    private lateinit var _presenter: ContactsPresenter<ContactsMvpView>
    private lateinit var _contactsViewModel: ContactsViewModel
    private lateinit var _contactsLiveData: ContactsLiveData

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(permission.READ_CONTACTS)
        private const val ARG_PHONE_NUMBER = "phoneNumber"
        private const val ARG_CONTACT_NAME = "contactName"

        fun newInstance(): ContactsFragment {
            return newInstance(null, null)
        }

        fun newInstance(phoneNumber: String?, contactNumber: String?): ContactsFragment {
            val fragment = ContactsFragment()
            val args = Bundle()
            args.putString(ARG_PHONE_NUMBER, phoneNumber)
            args.putString(ARG_CONTACT_NAME, contactNumber)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onGetAdapter(): ContactsAdapter {
        val contactsAdapter = ContactsAdapter(_activity)
        contactsAdapter.setOnContactItemClick { contact: Contact? -> _presenter.onContactItemClick(contact) }
        contactsAdapter.setOnContactItemLongClickListener { contact: Contact? -> _presenter.onContactItemLongClick(contact) }
        return contactsAdapter
    }

    override fun onGetPermissions(): Array<String> {
        return REQUIRED_PERMISSIONS
    }

    override fun onGetLoader(args: Bundle): Loader<Cursor> {
        val contactName = args.getString(ARG_CONTACT_NAME, null)
        val phoneNumber = args.getString(ARG_PHONE_NUMBER, null)
        return FavoritesAndContactsLoader(_activity, phoneNumber, contactName)
    }

    override fun onSetup() {
        super.onSetup()
        mLoaderId = 0

        _presenter = ContactsPresenter()
        _presenter.attach(this)

        _contactsLiveData = ContactsLiveData(_activity)
        _contactsLiveData.observe(viewLifecycleOwner, Observer { cursor -> mAdapter?.setCursor(cursor) })
//        load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun load(phoneNumber: String?, contactName: String?) {
        val args = Bundle()
        args.putString(ARG_PHONE_NUMBER, phoneNumber)
        args.putString(ARG_CONTACT_NAME, contactName)
        arguments = args
        load()
    }

    override fun openContact(contact: Contact) {
        ContactBottomDialogFragment.newInstance(contact).show(_activity.supportFragmentManager, ContactBottomDialogFragment.TAG)
    }
}