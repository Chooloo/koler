package com.chooloo.www.koler.ui.callactions

import com.chooloo.www.koler.ui.base.BaseBottomSheetDialogFragment

class CallActionsBottomFragment : BaseBottomSheetDialogFragment() {
    private lateinit var _callActionsFragment: CallActionsFragment

    companion object {
        const val TAG = "call_actions_bottom_fragment"

        fun newInstance() = CallActionsBottomFragment()
    }

    override fun onSetup() {
        _callActionsFragment = CallActionsFragment.newInstance().also {
            putFragment(it)
        }
    }
}