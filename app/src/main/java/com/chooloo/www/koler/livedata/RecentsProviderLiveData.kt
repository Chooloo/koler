package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.data.RecentsBundle

class RecentsProviderLiveData(
    context: Context
) : ContentProviderLiveData<RecentsContentResolver, RecentsBundle>(context) {
    override val contentResolver by lazy { RecentsContentResolver(context) }
}