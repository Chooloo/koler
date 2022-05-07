package com.chooloo.www.chooloolib.ui.accounts

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.adapter.AccountsAdapter
import com.chooloo.www.chooloolib.interactor.call.CallNavigationsInteractor
import com.chooloo.www.chooloolib.model.RawContactAccount
import com.chooloo.www.chooloolib.ui.briefcontact.BriefContactFragment.Companion.ARG_CONTACT_ID
import com.chooloo.www.chooloolib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountsFragment @Inject constructor() :
    ListFragment<RawContactAccount, AccountsViewState>() {

    override val viewState: AccountsViewState by activityViewModels()

    @Inject override lateinit var adapter: AccountsAdapter
    @Inject lateinit var callNavigations: CallNavigationsInteractor


    override fun onSetup() {
        viewState.onContactId(args.getLong(ARG_CONTACT_ID))
        super.onSetup()
        viewState.callEvent.observe(this@AccountsFragment) {
            it.ifNew?.let(callNavigations::call)
        }
        binding.itemsScrollView.fastScroller.isVisible = false
    }

    companion object {
        fun newInstance(contactId: Long? = null) = AccountsFragment().apply {
            arguments = Bundle().apply {
                contactId?.let { putLong(ARG_CONTACT_ID, it) }
            }
        }
    }
}