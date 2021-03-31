package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.PhoneContentResolver
import com.chooloo.www.koler.data.PhonesBundle

class PhoneProviderLiveData(
    context: Context,
    private val contactId: Long
) : ContentProviderLiveData<PhoneContentResolver, PhonesBundle>(context) {
    override val contentResolver by lazy { PhoneContentResolver(context, contactId) }
}