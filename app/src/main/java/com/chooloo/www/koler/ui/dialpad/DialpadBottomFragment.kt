package com.chooloo.www.koler.ui.dialpad

import android.os.Bundle
import com.chooloo.www.koler.ui.base.BaseBottomSheetDialogFragment

class DialpadBottomFragment : BaseBottomSheetDialogFragment() {
    companion object {
        const val ARG_DIALER = "is_dialer"

        fun newInstance(isDialer: Boolean) = DialpadBottomFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_DIALER, isDialer)
            }
        }
    }

    private val _isDialer by lazy { argsSafely.getBoolean(ARG_DIALER) }
    private val _dialpadFragment by lazy { DialpadFragment.newInstance(_isDialer) }

    var number: String
        get() = _dialpadFragment.number
        set(value) {
            _dialpadFragment.number = value
        }

    override fun onSetup() {
        putFragment(_dialpadFragment)
    }
}