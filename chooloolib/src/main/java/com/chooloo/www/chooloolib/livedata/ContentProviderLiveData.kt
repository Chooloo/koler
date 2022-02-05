package com.chooloo.www.chooloolib.livedata

import androidx.lifecycle.LiveData
import com.chooloo.www.chooloolib.contentresolver.BaseContentResolver
import io.reactivex.disposables.Disposable

abstract class ContentProviderLiveData<ContentResolver : BaseContentResolver<T>, T : Any> :
    LiveData<List<T>>() {

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