package com.chooloo.www.chooloolib.ui.prompt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PromptViewState @Inject constructor() : BaseViewState() {
    private val _title = MutableLiveData<String?>()
    private val _subtitle = MutableLiveData<String?>()
    private val _isActivated = MutableLiveData(false)

    val title: LiveData<String?> get() = _title
    val subtitle: LiveData<String?> get() = _subtitle
    val isActivated: LiveData<Boolean> get() = _isActivated


    fun onTitle(title: String?) {
        _title.value = title
    }

    fun onSubtitle(subtitle: String?) {
        _subtitle.value = subtitle
    }

    fun onActivated(isActivated: Boolean) {
        _isActivated.value = isActivated
    }
}