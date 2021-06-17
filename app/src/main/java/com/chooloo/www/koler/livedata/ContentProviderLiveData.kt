package com.chooloo.www.koler.livedata

import android.content.Context
import android.os.Handler
import androidx.lifecycle.LiveData
import com.chooloo.www.koler.contentresolver.BaseContentResolver

abstract class ContentProviderLiveData<ContentResolver : BaseContentResolver<T>, T : Any>(
    protected val context: Context,
) : LiveData<T>() {
    abstract val contentResolver: ContentResolver

    var filter: String?
        get() = contentResolver.filter
        set(value) {
            contentResolver.filter = value
            updateData()
        }

    override fun onActive() {
        contentResolver.apply {
            observe()
            setOnContentChangedListener { updateData() }
            updateData()
        }
    }

    override fun onInactive() {
        contentResolver.detach()
    }

    private fun updateData() {
        Handler(context.mainLooper).post {
            value = contentResolver.content
        }
    }
}