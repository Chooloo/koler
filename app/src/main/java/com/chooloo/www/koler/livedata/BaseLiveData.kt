package com.chooloo.www.koler.livedata

import android.content.Context
import androidx.lifecycle.LiveData

abstract class BaseLiveData<T:Any>(
        protected val context: Context
) : LiveData<T>() {
    abstract fun updateData()
}