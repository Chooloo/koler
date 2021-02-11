package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.ContactsContentResolver
import com.chooloo.www.koler.entity.Contact

class ContactsLiveData(context: Context) : BaseContentLiveData<ContactsContentResolver, Array<Contact>>(context) {

    companion object {
        val REQUIRED_PERMISSIONS = ContactsContentResolver.REQUIRED_PERMISSIONS
    }

    override fun onGetContentResolver(): ContactsContentResolver {
        return ContactsContentResolver(context)
    }
}