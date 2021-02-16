package com.chooloo.www.koler.livedata

import android.content.Context
import android.os.Handler
import androidx.lifecycle.LiveData
import com.chooloo.www.koler.contentresolver.BaseContentResolver

abstract class ContentProviderLiveData<C : BaseContentResolver<T>, T : Any>(
        protected val context: Context,
) : LiveData<T>() {
    private val _contentResolver: C by lazy { onGetContentResolver() }

    val requiredPermissions: Array<String>
        get() = _contentResolver.requiredPermissions

    override fun onActive() {
        _contentResolver.apply {
            observe()
            setOnContentChangedListener { updateData() }
        }
    }

    override fun onInactive() {
        _contentResolver.detach()
    }

    private fun updateData() {
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