package com.chooloo.www.koler.viewmodel.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private var _text: MutableLiveData<String?>
    private var _isFocused: MutableLiveData<Boolean>

    init {
        _text = MutableLiveData(null)
        _isFocused = MutableLiveData(false)
    }

    var text: MutableLiveData<String?>
        get() = _text
        set(value) {
            _text.value = if (value.value == "") null else value.value
        }

    var isFocused: MutableLiveData<Boolean>
        get() = _isFocused
        set(value) {
            _isFocused = value
        }
}