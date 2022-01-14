package com.chooloo.www.chooloolib.livedata

import android.content.Context
import com.chooloo.www.chooloolib.contentresolver.ContactsContentResolver
import com.chooloo.www.chooloolib.data.account.ContactAccount

class ContactsProviderLiveData(context: Context) :
    ContentProviderLiveData<ContactsContentResolver, ContactAccount>(context) {

    override val contentResolver by lazy { ContactsContentResolver(context) }
}