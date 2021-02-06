package com.chooloo.www.callmanager.livedata

import android.content.Context
import com.chooloo.www.callmanager.contentresolver.BaseContentResolver

abstract class BaseContentLiveData<C : BaseContentResolver<T>, T : Any>(context: Context) : BaseLiveData<T>(context) {
    private var contentResolver: C

    init {
        contentResolver = onGetContentResolver()
    }

    override fun onActive() {
        super.onActive()
        contentResolver.observe()
        contentResolver.setOnContentChangedListener { updateObservers() }
    }

    override fun onInactive() {
        super.onInactive()
        contentResolver.detach()
    }

    abstract fun onGetContentResolver(): C
}