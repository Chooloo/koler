package com.chooloo.www.koler.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.chooloo.www.koler.databinding.FragmentBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment(), MvpView {

    private lateinit var _binding: FragmentBottomDialogBinding
    protected lateinit var _activity: BaseActivity
    var isShown = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is BaseActivity) {
            throw TypeCastException("Fragment not a child of base activity")
        }
        _activity = context
        _activity.onAttachFragment(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBottomDialogBinding.inflate(inflater, container, false)
        _binding.bottomDialogFragmentCloseButton.setOnClickListener { dismiss() }
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetup()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isShown) {
            super.show(manager, tag)
            isShown = true
        }
    }

    override fun dismiss() {
        if (isShown) {
            super.dismiss()
            isShown = false
        }
    }

    override fun hasPermission(permission: String): Boolean {
        return false
    }

    override fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.filter { p -> _activity.hasPermission(p) }.isNotEmpty()
    }

    override fun askForPermission(permission: String, requestCode: Int?) {
        askForPermissions(arrayOf(permission), requestCode)
    }

    override fun askForPermissions(permissions: Array<String>, requestCode: Int?) {
        requestPermissions(permissions, requestCode ?: 1)
    }

    override fun showMessage(message: String) {
        _activity.showMessage(message)
    }

    override fun showMessage(@StringRes stringResId: Int) {
        _activity.showMessage(stringResId)
    }

    override fun showError(message: String) {
        _activity.showError(message)
    }

    override fun showError(@StringRes stringResId: Int) {
        _activity.showError(getString(stringResId))
    }

    protected fun putFragment(fragment: BaseFragment) {
        childFragmentManager.beginTransaction().replace(_binding.bottomDialogFragmentPlaceholder.id, fragment).commit()
    }

    protected val argsSafely: Bundle
        get() = arguments
                ?: throw IllegalArgumentException("Always create fragment with newInstance()")

}