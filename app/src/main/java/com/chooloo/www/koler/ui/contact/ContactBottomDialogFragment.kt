package com.chooloo.www.koler.ui.contact

import android.os.Bundle
import com.chooloo.www.koler.ui.base.BaseBottomSheetDialogFragment
import com.chooloo.www.koler.ui.contact.ContactFragment.Companion.ARG_CONTACT_ID

class ContactBottomDialogFragment : BaseBottomSheetDialogFragment() {
    private val _contactId by lazy { argsSafely.getLong(ARG_CONTACT_ID) }
    private val _contactFragment by lazy { ContactFragment.newInstance(_contactId) }

    companion object {
        const val TAG = "contact_bottom_dialog_fragment"

        fun newInstance(contactId: Long) = ContactBottomDialogFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_CONTACT_ID, contactId)
            }
        }
    }

    override fun onSetup() {
        putFragment(_contactFragment)
    }
}