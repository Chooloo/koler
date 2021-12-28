package com.chooloo.www.koler.livedata

import android.content.Context
import androidx.lifecycle.LiveData
import com.chooloo.www.koler.contentresolver.BaseContentResolver
import io.reactivex.disposables.Disposable

abstract class ContentProviderLiveData<ContentResolver : BaseContentResolver<ItemType>, ItemType : Any>(
    protected val context: Context,
) : LiveData<List<ItemType>>() {

    abstract val contentResolver: ContentResolver

    private var _observer: Disposable? = null

    var filter: String?
        get() = contentResolver.filter
        @Synchronized
        set(value) {
            contentResolver.filter = value
            attachObserver()
        }


    override fun onActive() {
        attachObserver()
    }

    override fun onInactive() {
        _observer?.dispose()
    }

    private fun attachObserver() {
        _observer?.dispose()
        _observer = contentResolver.observeItems(this::postValue)
    }
}