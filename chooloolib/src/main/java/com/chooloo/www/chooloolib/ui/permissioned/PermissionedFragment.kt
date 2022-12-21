package com.chooloo.www.chooloolib.ui.permissioned

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.databinding.PermissionedBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.permission.PermissionViewState

abstract class PermissionedFragment<out VM : PermissionedViewState> : BaseFragment<VM>() {

    override val contentView by lazy { _binding.root }

    private val _permissionViewState: PermissionViewState by viewModels()
    private val _binding by lazy { PermissionedBinding.inflate(layoutInflater) }
    private val _permissionFragment by lazy { fragmentFactory.getPermissionFragment() }


    override fun onSetup() {
        viewState.apply {
            if (permissions.hasSelfPermissions(requiredPermissions.toTypedArray())) {
                addMainFragment()
            } else {
                addPermissionsFragment()
                _permissionViewState.apply {
                    onPermissions(requiredPermissions)
                    permissionsTextRes?.let(::onTextRes)
                    permissionsImageRes?.let(::onImageRes)
                    isGranted.observe(this@PermissionedFragment) {
                        if (it) {
                            addMainFragment()
                        }
                        onIsPermissionGranted(it)
                    }
                }
            }
        }
    }

    private fun addMainFragment() {
        removePermissionsFragment()
        mainContentView.parent?.let { (it as ViewGroup).removeView(mainContentView) }
        _binding.root.addView(mainContentView)
        _onSetup()
    }

    private fun addPermissionsFragment() {
        _binding.permissionedPermissionContainer.isVisible = true

        childFragmentManager.beginTransaction()
            .add(
                _binding.permissionedPermissionContainer.id,
                fragmentFactory.getPermissionFragment(),
                PERMISSIONS_FRAGMENT_TAG
            )
            .commit()
    }

    private fun removePermissionsFragment() {
        childFragmentManager.findFragmentByTag(PERMISSIONS_FRAGMENT_TAG)?.let {
            childFragmentManager.beginTransaction().remove(it).commit()
        }

        _binding.permissionedPermissionContainer.isVisible = false
    }

    abstract val mainContentView: View


    companion object {
        const val PERMISSIONS_FRAGMENT_TAG = "permissions_fragment"
    }
}