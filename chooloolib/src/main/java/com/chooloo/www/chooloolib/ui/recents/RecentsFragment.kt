package com.chooloo.www.chooloolib.ui.recents

import android.os.Bundle
import com.chooloo.www.chooloolib.adapter.RecentsAdapter
import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.ui.list.ListFragment
import javax.inject.Inject

class RecentsFragment :
    ListFragment<RecentAccount, RecentsAdapter>(),
    RecentsContract.View {

    @Inject override lateinit var controller: RecentsContract.Controller<out RecentsFragment>


    companion object {
        fun newInstance(filter: String? = null) = RecentsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FILTER, filter)
            }
        }
    }
}