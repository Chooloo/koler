package com.chooloo.www.callmanager.livedata

import android.content.Context
import androidx.lifecycle.LiveData

abstract class BaseLiveData<T:Any>(
        protected val context: Context
) : LiveData<T>() {
    abstract fun updateData()
}