package com.chooloo.www.koler.ui.contact

import android.os.Bundle
import com.chooloo.www.koler.ui.base.BaseBottomSheetDialogFragment
import com.chooloo.www.koler.ui.contact.ContactFragment.Companion.ARG_CONTACT_ID

class ContactBottomDialogFragment : BaseBottomSheetDialogFragment() {
    private lateinit var _contactFragment: ContactFragment

    companion object {
        const val TAG = "contact_bottom_dialog_fragment"

        fun newInstance(contactId: Long): ContactBottomDialogFragment {
            return ContactBottomDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CONTACT_ID, contactId)
                }
            }
        }
    }

    override fun onSetup() {
        _contactFragment = ContactFragment.newInstance(argsSafely.getLong(ARG_CONTACT_ID)).also {
            putFragment(it)
        }
    }
}