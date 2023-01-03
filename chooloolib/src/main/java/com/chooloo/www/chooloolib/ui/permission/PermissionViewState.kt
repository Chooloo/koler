package com.chooloo.www.chooloolib.ui.permission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewState @Inject constructor(
    private val permissions: PermissionsInteractor
) : BaseViewState() {
    private val _textRes = MutableLiveData<Int>()
    private val _imageRes = MutableLiveData<Int>()
    private var _isGranted = MutableLiveData<Boolean>()
    private var _permissions: List<String> = emptyList()

    val textRes: LiveData<Int> = _textRes
    val imageRes: LiveData<Int> = _imageRes
    val isGranted: LiveData<Boolean> = _isGranted


    override fun attach() {
        super.attach()
        checkPermission()
    }

    private fun checkPermission() {
        viewModelScope.launch {
            _isGranted.value = permissions.checkPermissions(*_permissions.toTypedArray())
        }
    }

    fun onGrantClick() {
        checkPermission()
    }

    fun onTextRes(textRes: Int) {
        _textRes.value = textRes
    }

    fun onImageRes(imageRes: Int) {
        _imageRes.value = imageRes
    }

    fun onPermissions(permissions: List<String>) {
        _permissions = permissions
    }
}