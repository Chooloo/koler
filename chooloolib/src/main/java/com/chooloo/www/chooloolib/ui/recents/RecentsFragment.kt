package com.chooloo.www.chooloolib.ui.recents

import android.os.Bundle
import com.chooloo.www.chooloolib.adapter.RecentsAdapter
import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecentsFragment @Inject constructor() :
    ListFragment<RecentAccount, RecentsAdapter>(),
    RecentsContract.View {

    override lateinit var controller: RecentsContract.Controller


    override fun onSetup() {
        controller = controllerFactory.getRecentsController(this)
        super.onSetup()
    }

    companion object {
        fun newInstance(filter: String? = null) = RecentsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FILTER, filter)
            }
        }
    }
}