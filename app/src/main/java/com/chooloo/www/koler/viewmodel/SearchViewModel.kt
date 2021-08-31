package com.chooloo.www.koler.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    val number by lazy { MutableLiveData<String?>(null) }
    val recentsText by lazy { MutableLiveData<String?>(null) }
    val contactsText by lazy { MutableLiveData<String?>(null) }
}