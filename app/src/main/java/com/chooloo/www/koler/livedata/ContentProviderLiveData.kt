package com.chooloo.www.koler.livedata

import android.content.Context
import android.os.Handler
import androidx.lifecycle.LiveData
import com.chooloo.www.koler.contentresolver.BaseContentResolver

abstract class ContentProviderLiveData<C : BaseContentResolver<T>, T : Any>(
        protected val context: Context,
) : LiveData<T>() {
    protected var _contentResolver: C

    val requiredPermissions: Array<String>
        get() = _contentResolver.requiredPermissions

    init {
        _contentResolver = onGetContentResolver()
    }

    override fun onActive() {
        _contentResolver.observe()
        _contentResolver.setOnContentChangedListener { updateData() }
        updateData()
    }

    override fun onInactive() {
        _contentResolver.detach()
    }

    fun updateData() {
        Handler(context.mainLooper).post {
            value = _contentResolver.content
        }
    }

    fun setFilter(filter: String?) {
        _contentResolver.setFilter(filter)
        updateData()
    }

    abstract fun onGetContentResolver(): C
}