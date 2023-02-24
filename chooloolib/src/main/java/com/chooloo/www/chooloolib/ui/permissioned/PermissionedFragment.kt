package com.chooloo.www.chooloolib.ui.permissioned

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.databinding.PermissionedBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import com.chooloo.www.chooloolib.ui.permission.PermissionViewState

abstract class PermissionedFragment<out VM : PermissionedViewState> : BaseFragment<VM>() {

    override val contentView by lazy { _binding.root }

    private val _permissionViewState: PermissionViewState by viewModels()
    private val _binding by lazy { PermissionedBinding.inflate(layoutInflater) }
    private val _permissionFragment by lazy { fragmentFactory.getPermissionFragment() }


    override fun onSetupHook() {
        _permissionViewState.apply {
            onPermissions(viewState.requiredPermissions)
            viewState.permissionsTextRes?.let(::onTextRes)
            viewState.permissionsImageRes?.let(::onImageRes)
            isGranted.observe(this@PermissionedFragment, viewState::onIsPermissionGranted)
        }

        viewState.shouldShowMainFragment.observe(this@PermissionedFragment) {
            if (it) {
                showMainFragment()
            } else {
                showPermissionsFragment()
            }
        }

        viewState.attachHook()
    }

    private fun showMainFragment() {
        childFragmentManager.findFragmentByTag(PERMISSIONS_FRAGMENT_TAG)?.let { fragment ->
            childFragmentManager.commit { remove(fragment) }
        }

        _binding.permissionedPermissionContainer.isVisible = false
        mainContentView.parent?.let { (it as ViewGroup).removeView(mainContentView) }
        _binding.root.addView(mainContentView)
        super.onSetupHook()
    }

    private fun showPermissionsFragment() {
        _binding.root.removeView(mainContentView)
        _binding.permissionedPermissionContainer.isVisible = true

        childFragmentManager.findFragmentByTag(PERMISSIONS_FRAGMENT_TAG)?.let { } ?: run {
            childFragmentManager.commit {
                add(
                    _binding.permissionedPermissionContainer.id,
                    fragmentFactory.getPermissionFragment(),
                    PERMISSIONS_FRAGMENT_TAG
                )
            }
        }
    }

    abstract val mainContentView: View


    companion object {
        const val PERMISSIONS_FRAGMENT_TAG = "permissions_fragment"
    }
}