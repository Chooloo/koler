package com.chooloo.www.koler.livedata

import android.content.Context
import android.os.Handler
import com.chooloo.www.koler.contentresolver.BaseContentResolver

abstract class BaseContentLiveData<C : BaseContentResolver<T>, T : Any>(context: Context) : BaseLiveData<T>(context) {
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

    override fun updateData() {
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

    abstract fun onGetContentResolver(): C
}