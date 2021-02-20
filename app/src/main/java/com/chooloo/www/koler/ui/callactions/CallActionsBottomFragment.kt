package com.chooloo.www.koler.ui.callactions

import com.chooloo.www.koler.ui.base.BaseBottomSheetDialogFragment

class CallActionsBottomFragment : BaseBottomSheetDialogFragment() {
    private val _callActionsFragment by lazy { CallActionsFragment.newInstance() }

    companion object {
        const val TAG = "call_actions_bottom_fragment"

        fun newInstance() = CallActionsBottomFragment()
    }

    override fun onSetup() {
        putFragment(_callActionsFragment)
    }
}