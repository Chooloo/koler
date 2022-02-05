package com.chooloo.www.chooloolib.livedata

import com.chooloo.www.chooloolib.contentresolver.RecentsContentResolver
import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.di.factory.contentresolver.ContentResolverFactory

class RecentsProviderLiveData(
    private val contentResolverFactory: ContentResolverFactory
) : ContentProviderLiveData<RecentsContentResolver, RecentAccount>() {
    override val contentResolver by lazy { contentResolverFactory.getRecentsContentResolver() }
}