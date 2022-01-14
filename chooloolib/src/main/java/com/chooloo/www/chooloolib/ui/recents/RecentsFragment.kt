package com.chooloo.www.chooloolib.ui.recents

import android.os.Bundle
import com.chooloo.www.chooloolib.adapter.RecentsAdapter
import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.ui.base.BottomFragment
import com.chooloo.www.chooloolib.ui.list.ListFragment
import com.chooloo.www.chooloolib.ui.recent.RecentFragment

class RecentsFragment :
    ListFragment<RecentAccount, RecentsAdapter>(),
    RecentsContract.View {

    override val controller: RecentsController<out RecentsFragment> by lazy {
        RecentsController(this)
    }

    override fun onSetup() {
        controller.initialize()
        super.onSetup()
    }

    override fun openRecent(recent: RecentAccount) {
        BottomFragment(RecentFragment.newInstance(recent.id)).show(
            baseActivity.supportFragmentManager,
            RecentFragment.TAG
        )
    }

    companion object {
        fun newInstance(filter: String? = null) = RecentsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FILTER, filter)
            }
        }
    }
}