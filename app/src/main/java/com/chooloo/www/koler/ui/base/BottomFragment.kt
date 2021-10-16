package com.chooloo.www.koler.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.chooloo.www.koler.databinding.BottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


open class BottomFragment<FragmentType : Fragment>(
    val fragment: FragmentType
) : BottomSheetDialogFragment(), BaseContract.View {
    override val boundComponent
        get() = baseActivity.boundComponent

    private val _binding by lazy { BottomDialogBinding.inflate(layoutInflater) }

    protected val argsSafely get() = arguments ?: Bundle()
    protected val baseActivity by lazy { context as BaseActivity }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is BaseActivity) {
            throw TypeCastException("Fragment not a child of base activity")
        }
        baseActivity.onAttachFragment(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showsDialog = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = _binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetup()
    }

    override fun onSetup() {
        childFragmentManager.beginTransaction()
            .replace(_binding.bottomDialogFragmentPlaceholder.id, fragment).commit()
    }


    //region base view

    override fun showMessage(message: String) {
        baseActivity.showMessage(message)
    }

    override fun showMessage(@StringRes stringResId: Int) {
        baseActivity.showMessage(stringResId)
    }

    override fun showError(message: String) {
        baseActivity.showError(message)
    }

    override fun showError(@StringRes stringResId: Int) {
        baseActivity.showError(getString(stringResId))
    }

    override fun getColor(color: Int) =
        baseActivity.getColor(color)

    override fun hasPermission(permission: String) =
        baseActivity.hasPermission(permission)

    override fun hasPermissions(permissions: Array<String>) =
        permissions.any { p -> baseActivity.hasPermission(p) }

    //endregion
}