package com.chooloo.www.koler.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private var _text: MutableLiveData<String?> = MutableLiveData(null)
    private var _number: MutableLiveData<String?> = MutableLiveData(null)
    private var _isFocused: MutableLiveData<Boolean> = MutableLiveData(false)

    var text: MutableLiveData<String?>
        get() = _text
        set(value) {
            _text.value = if (value.value == "") null else value.value
        }

    var number: MutableLiveData<String?>
        get() = _number
        set(value) {
            _number.value = if (value.value == "") null else value.value
        }

    var isFocused: MutableLiveData<Boolean>
        get() = _isFocused
        set(value) {
            _isFocused = value
        }
}