package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.entity.RecentsBundle

class RecentsProviderLiveData(
    context: Context
) : ContentProviderLiveData<RecentsContentResolver, RecentsBundle>(context) {
    override fun onGetContentResolver() = RecentsContentResolver(context)
}