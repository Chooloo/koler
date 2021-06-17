package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.data.Contact

class ContactsProviderLiveData(context: Context) :
    ContentProviderLiveData<ContactsContentResolver, ArrayList<Contact>>(context) {
    override val contentResolver by lazy { ContactsContentResolver(context) }
}