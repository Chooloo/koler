package com.chooloo.www.chooloolib.livedata

import android.content.Context
import android.database.ContentObserver
import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.contentresolver.BaseContentResolver
import dagger.hilt.android.qualifiers.ApplicationContext

abstract class ContentProviderLiveData<ContentResolver : BaseContentResolver<T>, T : Any>(
    @ApplicationContext private val context: Context
) :
    LiveData<List<T>>() {

    private var observer = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            postValue(contentResolver.queryItems())
        }
    }

    var filter: String?
        get() = contentResolver.filter
        @Synchronized
        set(value) {
            contentResolver.filter = value
            onActive()
        }


    override fun onActive() {
        detachObserver()
        attachObserver()
        value = contentResolver.queryItems()
    }

    override fun onInactive() {
        detachObserver()
    }

    private fun attachObserver() {
        context.contentResolver.registerContentObserver(contentResolver.finalUri, true, observer)
    }

    private fun detachObserver() {
        context.contentResolver.unregisterContentObserver(observer)
    }


    abstract val contentResolver: ContentResolver
}