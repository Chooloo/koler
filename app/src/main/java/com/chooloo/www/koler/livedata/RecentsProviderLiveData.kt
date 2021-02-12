package com.chooloo.www.koler.livedata

import android.content.Context
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.entity.Recent

class RecentsProviderLiveData(context: Context) : ContentProviderLiveData<RecentsContentResolver, Array<Recent>>(context) {
    override fun onGetContentResolver() = RecentsContentResolver(context)
}