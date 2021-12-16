package com.chooloo.www.koler.ui.phones

import android.os.Bundle
import com.chooloo.www.koler.adapter.PhonesAdapter
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.ui.contact.ContactFragment.Companion.ARG_CONTACT_ID
import com.chooloo.www.koler.ui.list.ListFragment

class PhonesFragment :
    ListFragment<PhoneAccount, PhonesAdapter>(),
    PhonesContract.View {

    override val contactId by lazy { args.getLong(ARG_CONTACT_ID) }
    override lateinit var controller: PhonesController<PhonesFragment>


    override fun onSetup() {
        controller = PhonesController(this)
        super.onSetup()
    }

    override fun showItem(item: PhoneAccount) {
        return
    }


    companion object {
        fun newInstance(
            contactId: Long? = null,
            isHideNoResults: Boolean = false
        ) =
            PhonesFragment().apply {
                arguments = Bundle().apply {
                    contactId?.let { putLong(ARG_CONTACT_ID, it) }
                    putBoolean(ARG_IS_HIDE_NO_RESULTS, isHideNoResults)
                }
            }
    }
}