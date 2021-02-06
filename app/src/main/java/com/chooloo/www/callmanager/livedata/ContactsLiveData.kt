package com.chooloo.www.callmanager.livedata

import android.content.Context
import com.chooloo.www.callmanager.contentresolver.ContactsContentResolver
import com.chooloo.www.callmanager.entity.Contact

class ContactsLiveData(context: Context) : BaseContentLiveData<ContactsContentResolver, Array<Contact>>(context) {

    companion object {
        val REQUIRED_PERMISSION = ContactsContentResolver.REQUIRED_PERMISSION
    }

    override fun onGetContentResolver(): ContactsContentResolver {
        return ContactsContentResolver(context)
    }
}