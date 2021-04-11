package com.chooloo.www.koler.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    val recentsText by lazy { MutableLiveData<String?>(null) }
    val contactsText by lazy { MutableLiveData<String?>(null) }
    val number by lazy { MutableLiveData<String?>(null) }
}