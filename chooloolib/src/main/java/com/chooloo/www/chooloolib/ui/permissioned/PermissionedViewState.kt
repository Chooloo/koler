package com.chooloo.www.chooloolib.ui.permissioned

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.ui.base.BaseViewState

abstract class PermissionedViewState : BaseViewState() {
    private val _isPermissionGranted = MutableLiveData<Boolean>()

    open val permissionsTextRes: Int? = null
    open val permissionsImageRes: Int? = null
    open val requiredPermissions: List<String> = emptyList()

    val isPermissionGranted: LiveData<Boolean> = _isPermissionGranted


    fun onIsPermissionGranted(isGranted: Boolean) {
        _isPermissionGranted.value = isGranted
        if (isGranted) {
            _attach()
        }
    }
}