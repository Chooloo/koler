package com.chooloo.www.chooloolib.livedata

import android.content.Context
import com.chooloo.www.chooloolib.contentresolver.PhonesContentResolver
import com.chooloo.www.chooloolib.data.account.PhoneAccount

class PhonesProviderLiveData(context: Context, private val contactId: Long? = null) :
    ContentProviderLiveData<PhonesContentResolver, PhoneAccount>(context) {

    override val contentResolver by lazy { PhonesContentResolver(context, contactId) }
}