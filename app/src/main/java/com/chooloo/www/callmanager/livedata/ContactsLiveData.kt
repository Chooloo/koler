package com.chooloo.www.callmanager.livedata

import android.content.Context
import androidx.lifecycle.LiveData
import com.chooloo.www.callmanager.contentresolver.ContactsContentResolver
import com.chooloo.www.callmanager.entity.Contact

class ContactsLiveData(
        private val context: Context
) : LiveData<Array<Contact>>() {

    private val contactsContentResolver = ContactsContentResolver(context)

    override fun onActive() {
        contactsContentResolver.observe()
        contactsContentResolver.setOnContentChangedListener { postValue(ContactsContentResolver.getContacts(context)) }
    }

    override fun onInactive() {
        contactsContentResolver.detach()
    }
}