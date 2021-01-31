package com.chooloo.www.callmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class DialViewModel(application: Application) : AndroidViewModel(application) {
    val number: MutableLiveData<String?>

    fun setNumber(number: String) {
        this.number.value = if (number === "") null else number
    }

    init {
        number = MutableLiveData(null)
    }
}