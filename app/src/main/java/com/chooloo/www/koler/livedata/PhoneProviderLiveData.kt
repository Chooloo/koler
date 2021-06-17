package com.chooloo.www.koler.livedata

import PhoneAccount
import android.content.Context
import com.chooloo.www.koler.contentresolver.PhoneContentResolver

class PhoneProviderLiveData(
    context: Context,
    private val contactId: Long
) : ContentProviderLiveData<PhoneContentResolver, ArrayList<PhoneAccount>>(context) {
    override val contentResolver by lazy { PhoneContentResolver(context, contactId) }
}