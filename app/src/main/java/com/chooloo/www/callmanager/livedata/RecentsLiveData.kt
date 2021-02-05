package com.chooloo.www.callmanager.livedata

import android.content.Context
import androidx.lifecycle.LiveData
import com.chooloo.www.callmanager.contentresolver.RecentsContentResolver
import com.chooloo.www.callmanager.entity.Recent

class RecentsLiveData(
        private val context: Context
) : LiveData<Array<Recent>>() {

    private val recentsContentResolver = RecentsContentResolver(context)

    override fun onActive() {
        recentsContentResolver.observe()
        recentsContentResolver.setOnContentChangedListener { postValue(RecentsContentResolver.getRecents(context)) }
    }

    override fun onInactive() {
        recentsContentResolver.detach()
    }
}