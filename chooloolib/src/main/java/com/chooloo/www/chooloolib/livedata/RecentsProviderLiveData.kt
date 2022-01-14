package com.chooloo.www.chooloolib.livedata

import android.content.Context
import com.chooloo.www.chooloolib.contentresolver.RecentsContentResolver
import com.chooloo.www.chooloolib.data.account.RecentAccount

class RecentsProviderLiveData(context: Context) :
    ContentProviderLiveData<RecentsContentResolver, RecentAccount>(context) {

    override val contentResolver by lazy { RecentsContentResolver(context) }
}