package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.data.account.ContactAccount

class ContactsProviderLiveData(context: Context) :
    ContentProviderLiveData<ContactsContentResolver, ContactAccount>(context) {

    override val contentResolver by lazy { ContactsContentResolver(context) }
}