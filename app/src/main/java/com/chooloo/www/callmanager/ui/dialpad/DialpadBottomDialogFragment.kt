package com.chooloo.www.callmanager.ui.dialpad

import android.os.Bundle
import com.chooloo.www.callmanager.ui.base.BaseBottomSheetDialogFragment
import com.chooloo.www.callmanager.ui.dialpad.DialpadFragment.OnKeyDownListener

class DialpadBottomDialogFragment : BaseBottomSheetDialogFragment() {
    private lateinit var _dialpadFragment: DialpadFragment

    companion object {
        const val ARG_DIALER = "is_dialer"

        @JvmStatic
        fun newInstance(isDialer: Boolean): DialpadBottomDialogFragment {
            return DialpadBottomDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_DIALER, isDialer)
                }
            }
        }
    }

    var number: String
        get() = _dialpadFragment.number
        set(value) {
            _dialpadFragment.number = value
        }

    override fun onSetup() {
        _dialpadFragment = DialpadFragment.newInstance(argsSafely.getBoolean(ARG_DIALER))
        putFragment(_dialpadFragment)
    }

    fun setOnKeyDownListener(onKeyDownListener: OnKeyDownListener) {
        _dialpadFragment.setOnKeyDownListener(onKeyDownListener)
    }
}