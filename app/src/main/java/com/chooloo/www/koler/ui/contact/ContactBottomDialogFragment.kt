package com.chooloo.www.koler.ui.contact

import android.os.Bundle
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.ui.base.BaseBottomSheetDialogFragment

class ContactBottomDialogFragment : BaseBottomSheetDialogFragment() {
    private lateinit var _contactFragment: ContactFragment

    companion object {
        const val TAG = "contact_bottom_dialog_fragment"
        const val ARG_CONTACT = "contact"

        fun newInstance(contact: Contact): ContactBottomDialogFragment {
            return ContactBottomDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CONTACT, contact)
                }
            }
        }
    }

    override fun onSetup() {
        _contactFragment = ContactFragment.newInstance(argsSafely.getSerializable(ARG_CONTACT) as Contact).also {
            putFragment(it)
        }
    }
}