package com.chooloo.www.chooloolib.ui.phones

import android.os.Bundle
import com.chooloo.www.chooloolib.adapter.PhonesAdapter
import com.chooloo.www.chooloolib.data.account.PhoneAccount
import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactFragment.Companion.ARG_CONTACT_ID
import com.chooloo.www.chooloolib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhonesFragment @Inject constructor() :
    ListFragment<PhoneAccount, PhonesAdapter>(),
    PhonesContract.View {

    override val contactId by lazy { args.getLong(ARG_CONTACT_ID) }
    override val controller by lazy { controllerFactory.getPhonesController(this) }


    companion object {
        fun newInstance(contactId: Long? = null) = PhonesFragment().apply {
            arguments = Bundle().apply {
                contactId?.let { putLong(ARG_CONTACT_ID, it) }
            }
        }
    }
}