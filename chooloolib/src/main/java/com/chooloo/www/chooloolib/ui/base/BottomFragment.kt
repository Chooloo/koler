package com.chooloo.www.chooloolib.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.databinding.BottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

open class BottomFragment<FragmentType : BaseFragment<out BaseViewState>>(
    private val fragment: FragmentType
) : BottomSheetDialogFragment(), BaseView<BaseViewState> {
    override val viewState: BaseViewState by viewModels()

    private val binding by lazy { BottomDialogBinding.inflate(layoutInflater) }

    @Inject lateinit var baseActivity: BaseActivity<*>


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
        fragment.setOnFinishListener {
            this@BottomFragment.dismiss()
        }

        viewState.apply {
            attach()

            errorEvent.observe(this@BottomFragment) {
                it.ifNew?.let(this@BottomFragment::showError)
            }

            messageEvent.observe(this@BottomFragment) {
                it.ifNew?.let(this@BottomFragment::showMessage)
            }
        }

        childFragmentManager.beginTransaction()
            .replace(binding.bottomDialogFragmentPlaceholder.id, fragment).commit()
    }

    override fun showError(@StringRes stringResId: Int) {
        baseActivity.viewState.errorEvent.call(stringResId)
    }

    override fun showMessage(@StringRes stringResId: Int) {
        baseActivity.viewState.messageEvent.call(stringResId)
    }
}