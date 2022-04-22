package com.chooloo.www.chooloolib.livedata

import android.content.Context
import com.chooloo.www.chooloolib.contentresolver.ContactsContentResolver
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.model.ContactAccount
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Singleton
class ContactsLiveData(
    @ApplicationContext context: Context,
    private val contactId: Long? = null,
    private val contentResolverFactory: ContentResolverFactory
) : ContentProviderLiveData<ContactsContentResolver, ContactAccount>(context) {
    override val contentResolver by lazy { contentResolverFactory.getContactsContentResolver(contactId) }
}