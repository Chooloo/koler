package com.chooloo.www.chooloolib.livedata

import com.chooloo.www.chooloolib.contentresolver.PhonesContentResolver
import com.chooloo.www.chooloolib.data.account.PhoneAccount
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory

class PhonesProviderLiveData(
    private val contactId: Long? = null,
    private val contentResolverFactory: ContentResolverFactory,
) : ContentProviderLiveData<PhonesContentResolver, PhoneAccount>() {
    override val contentResolver by lazy { contentResolverFactory.getPhonesContentResolver(contactId) }
}