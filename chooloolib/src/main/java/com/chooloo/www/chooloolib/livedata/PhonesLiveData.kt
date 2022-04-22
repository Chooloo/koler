package com.chooloo.www.chooloolib.livedata

import android.content.Context
import com.chooloo.www.chooloolib.contentresolver.PhonesContentResolver
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory
import com.chooloo.www.chooloolib.model.PhoneAccount

class PhonesLiveData(
    context: Context,
    private val contactId: Long? = null,
    private val contentResolverFactory: ContentResolverFactory,
) : ContentProviderLiveData<PhonesContentResolver, PhoneAccount>(context) {
    override val contentResolver by lazy { contentResolverFactory.getPhonesContentResolver(contactId) }
}