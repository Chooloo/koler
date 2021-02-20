package com.chooloo.www.koler.ui.recent

import android.os.Bundle
import com.chooloo.www.koler.ui.base.BaseBottomSheetDialogFragment
import com.chooloo.www.koler.ui.recent.RecentFragment.Companion.ARG_RECENT_ID

class RecentBottomDialogFragment : BaseBottomSheetDialogFragment() {

    private val _recentId by lazy { argsSafely.getLong(ARG_RECENT_ID) }
    private val _recentFragment by lazy { RecentFragment.newInstance(_recentId) }

    companion object {
        const val TAG = "recent_bottom_dialog_fragment"

        fun newInstance(recentId: Long) = RecentBottomDialogFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_RECENT_ID, recentId)
            }
        }
    }

    override fun onSetup() {
        putFragment(_recentFragment)
    }
}