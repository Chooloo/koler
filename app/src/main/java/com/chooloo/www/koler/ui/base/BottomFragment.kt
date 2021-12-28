package com.chooloo.www.koler.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.chooloo.www.koler.databinding.BottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


open class BottomFragment<FragmentType : BaseFragment>(
    private val fragment: FragmentType
) : BottomSheetDialogFragment(), BaseContract.View {
    protected val baseActivity by lazy { context as BaseActivity }
    private val binding by lazy { BottomDialogBinding.inflate(layoutInflater) }

    override val component get() = baseActivity.component


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is BaseActivity) {
            throw TypeCastException("Fragment not a child of base activity")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showsDialog = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSetup()
    }

    override fun onSetup() {
        fragment.setOnFinishListener { this@BottomFragment.dismiss() }
        childFragmentManager.beginTransaction()
            .replace(binding.bottomDialogFragmentPlaceholder.id, fragment).commit()
    }

    override fun showError(@StringRes stringResId: Int) {
        baseActivity.showError(stringResId)
    }

    override fun showMessage(@StringRes stringResId: Int) {
        baseActivity.showMessage(stringResId)
    }
}