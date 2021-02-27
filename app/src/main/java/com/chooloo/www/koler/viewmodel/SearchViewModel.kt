package com.chooloo.www.koler.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    val text by lazy { MutableLiveData<String?>(null) }
    val number by lazy { MutableLiveData<String?>(null) }
}