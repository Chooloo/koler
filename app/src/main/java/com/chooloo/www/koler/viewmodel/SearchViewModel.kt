package com.chooloo.www.koler.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    val text: MutableLiveData<String?> by lazy { MutableLiveData<String?>(null) }
    val number: MutableLiveData<String?> by lazy { MutableLiveData<String?>(null) }
}