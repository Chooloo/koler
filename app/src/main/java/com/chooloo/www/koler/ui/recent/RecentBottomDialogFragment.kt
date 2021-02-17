package com.chooloo.www.koler.ui.recent

import android.os.Bundle
import com.chooloo.www.koler.ui.base.BaseBottomSheetDialogFragment
import com.chooloo.www.koler.ui.contact.ContactFragment.Companion.ARG_CONTACT_ID

class RecentBottomDialogFragment : BaseBottomSheetDialogFragment() {
    private lateinit var _contactFragment: RecentFragment

    companion object {
        const val TAG = "contact_bottom_dialog_fragment"

        fun newInstance(contactId: Long): RecentBottomDialogFragment {
            return RecentBottomDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CONTACT_ID, contactId)
                }
            }
        }
    }

    override fun onSetup() {
        _contactFragment = RecentFragment.newInstance(argsSafely.getLong(ARG_CONTACT_ID)).also {
            putFragment(it)
        }
    }
}