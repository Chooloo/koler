package com.chooloo.www.chooloolib.ui.permissioned

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseViewState

abstract class PermissionedViewState(
    private val permissions: PermissionsInteractor
) : BaseViewState() {
    private val _isPermissionGranted = MutableLiveData<Boolean>()
    private val _shouldShowMainFragment = MutableLiveData<Boolean>()

    open val permissionsTextRes: Int? = null
    open val permissionsImageRes: Int? = null
    open val requiredPermissions: List<String> = emptyList()

    val isPermissionGranted: LiveData<Boolean> = _isPermissionGranted
    val shouldShowMainFragment: LiveData<Boolean> = _shouldShowMainFragment


    fun attachHook() {
        _shouldShowMainFragment.value =
            permissions.hasSelfPermissions(requiredPermissions.toTypedArray())
    }

    fun onIsPermissionGranted(isGranted: Boolean) {
        _isPermissionGranted.value = isGranted
        _shouldShowMainFragment.value = isGranted
    }
}