package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.entity.Contact

class ContactsProviderLiveData(context: Context) : ContentProviderLiveData<ContactsContentResolver, Array<Contact>>(context) {
    override fun onGetContentResolver() = ContactsContentResolver(context)
}