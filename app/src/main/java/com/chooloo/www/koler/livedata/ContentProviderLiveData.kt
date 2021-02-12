package com.chooloo.www.koler.livedata

import android.content.Context
import android.os.Handler
import androidx.lifecycle.LiveData
import com.chooloo.www.koler.contentresolver.BaseContentResolver

abstract class ContentProviderLiveData<C : BaseContentResolver<T>, T : Any>(
        protected val context: Context,
) : LiveData<T>() {
    private var contentResolver: C

    init {
        contentResolver = onGetContentResolver()
    }

    override fun onActive() {
        contentResolver.observe()
        contentResolver.setOnContentChangedListener { updateData() }
        updateData()
    }

    override fun onInactive() {
        contentResolver.detach()
    }

    fun updateData() {
        Handler(context.mainLooper).post {
            value = contentResolver.getContent()
        }
    }

    fun filter(filterString: String) {
        contentResolver.filter(filterString)
    }

    fun resetFilter() {
        contentResolver.reset()
    }

    fun getRequiredPermissions(): Array<String> = contentResolver.getRequiredPermissions()

    abstract fun onGetContentResolver(): C
}