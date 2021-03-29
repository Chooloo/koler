package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.PhonesContentResolver
import com.chooloo.www.koler.data.PhonesBundle

class PhonesProviderLiveData(
    context: Context,
    private val contactId: Long
) : ContentProviderLiveData<PhonesContentResolver, PhonesBundle>(context) {
    override fun onGetContentResolver() = PhonesContentResolver(context, contactId)
}