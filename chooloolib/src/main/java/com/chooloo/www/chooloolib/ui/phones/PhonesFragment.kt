package com.chooloo.www.chooloolib.ui.phones

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.adapter.PhonesAdapter
import com.chooloo.www.chooloolib.interactor.call.CallNavigationsInteractor
import com.chooloo.www.chooloolib.model.PhoneAccount
import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactFragment.Companion.ARG_CONTACT_ID
import com.chooloo.www.chooloolib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhonesFragment @Inject constructor() : ListFragment<PhoneAccount, PhonesViewState>() {
    override val viewState: PhonesViewState by activityViewModels()

    @Inject lateinit var callNavigations: CallNavigationsInteractor
    @Inject override lateinit var adapter: PhonesAdapter


    override fun onSetup() {
        viewState.onContactId(args.getLong(ARG_CONTACT_ID))
        super.onSetup()
        viewState.callEvent.observe(this@PhonesFragment) {
            it.ifNew?.let(callNavigations::call)
        }
        binding.itemsScrollView.fastScroller.isVisible = false
    }

    companion object {
        fun newInstance(contactId: Long? = null) = PhonesFragment().apply {
            arguments = Bundle().apply {
                contactId?.let { putLong(ARG_CONTACT_ID, it) }
            }
        }
    }
}