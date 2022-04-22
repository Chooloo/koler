package com.chooloo.www.chooloolib.livedata

import android.content.Context
import com.chooloo.www.chooloolib.contentresolver.RawContactsContentResolver
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.model.RawContactAccount

class RawContactsLiveData(
    context: Context,
    private val contactId: Long,
    private val contentResolverFactory: ContentResolverFactory,
) : ContentProviderLiveData<RawContactsContentResolver, RawContactAccount>(context) {
    override val contentResolver by lazy {
        contentResolverFactory.getRawContactsContentResolver(contactId)
    }
}