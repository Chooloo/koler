package com.chooloo.www.koler.livedata

import android.content.Context
import androidx.lifecycle.LiveData
import com.chooloo.www.koler.contentresolver.BaseContentResolver
import io.reactivex.disposables.Disposable

abstract class ContentProviderLiveData<ContentResolver : BaseContentResolver<T>, T : Any>(
    protected val context: Context,
) : LiveData<T>() {
    abstract val contentResolver: ContentResolver

    private var _disposable: Disposable? = null

    var filter: String?
        get() = contentResolver.filter
        set(value) {
            contentResolver.filter = value
            contentResolver.queryContent {
                onInactive()
                onActive()
            }
        }


    override fun onActive() {
        _disposable = contentResolver.observeContent { postValue(it) }
    }

    override fun onInactive() {
        _disposable?.dispose()
    }
}