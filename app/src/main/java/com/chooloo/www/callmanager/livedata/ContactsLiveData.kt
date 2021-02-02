package com.chooloo.www.callmanager.livedata

import android.content.Context
import androidx.lifecycle.LiveData
import com.chooloo.www.callmanager.contentresolver.BaseContentResolver
import com.chooloo.www.callmanager.contentresolver.ContactsContentResolver
import com.chooloo.www.callmanager.entity.Contact

class ContactsLiveData(
        private val context: Context
) : LiveData<Array<Contact>>() {

    private var contactsContentResolver: ContactsContentResolver

    init {
        contactsContentResolver = ContactsContentResolver(context)
        contactsContentResolver.setOnContentChangedListener(object : BaseContentResolver.OnContentChangedListener<Array<Contact>> {
            override fun onContentChanged(content: Array<Contact>) {
                postValue(ContactsContentResolver.getContacts(context))
            }
        })
    }

    override fun onActive() {
        contactsContentResolver.observe()
    }

    override fun onInactive() {
        contactsContentResolver.detach()
    }
}