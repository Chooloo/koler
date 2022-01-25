package com.chooloo.www.chooloolib.ui.phones

import android.os.Bundle
import com.chooloo.www.chooloolib.adapter.PhonesAdapter
import com.chooloo.www.chooloolib.data.account.PhoneAccount
import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactFragment.Companion.ARG_CONTACT_ID
import com.chooloo.www.chooloolib.ui.list.ListFragment
import javax.inject.Inject

class PhonesFragment :
    ListFragment<PhoneAccount, PhonesAdapter>(),
    PhonesContract.View {

    override val contactId by lazy { args.getLong(ARG_CONTACT_ID) }

    @Inject override lateinit var controller: PhonesContract.Controller<PhonesFragment>


    companion object {
        fun newInstance(contactId: Long? = null) = PhonesFragment().apply {
            arguments = Bundle().apply {
                contactId?.let { putLong(ARG_CONTACT_ID, it) }
            }
        }
    }
}