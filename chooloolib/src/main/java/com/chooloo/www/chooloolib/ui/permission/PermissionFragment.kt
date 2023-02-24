package com.chooloo.www.chooloolib.ui.permission

import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.databinding.PermissionBinding
import com.chooloo.www.chooloolib.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionFragment @Inject constructor() : BaseFragment<PermissionViewState>() {
    override val contentView by lazy { _binding.root }
    override val viewState: PermissionViewState by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private val _binding by lazy { PermissionBinding.inflate(layoutInflater) }


    override fun onSetup() {
        viewState.apply {
            textRes.observe(this@PermissionFragment, _binding.noPermissionText::setText)
            imageRes.observe(this@PermissionFragment) {
                _binding.noPermissionImage.setImageResource(it)
            }
        }

        _binding.apply {
            noPermissionButton.setOnClickListener { viewState.onGrantClick() }
        }
    }
}