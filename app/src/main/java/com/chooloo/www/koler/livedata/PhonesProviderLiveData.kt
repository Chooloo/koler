package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.PhonesContentResolver
import com.chooloo.www.koler.data.account.PhoneAccount

class PhonesProviderLiveData(context: Context, private val contactId: Long? = null) :
    ContentProviderLiveData<PhonesContentResolver, ArrayList<PhoneAccount>>(context) {

    override val contentResolver by lazy { PhonesContentResolver(context, contactId) }
}