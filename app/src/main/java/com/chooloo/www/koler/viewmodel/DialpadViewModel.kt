package com.chooloo.www.koler.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialpadViewModel : ViewModel() {
    val number by lazy { MutableLiveData<String?>(null) }
}