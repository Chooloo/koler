package com.chooloo.www.koler.ui.recents

import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.data.account.RecentAccount
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.ui.recent.RecentFragment

class RecentsFragment : ListFragment<RecentAccount, RecentsAdapter>(), ListContract.View<RecentAccount> {
    override lateinit var controller: RecentsController<RecentsFragment>


    override fun onSetup() {
        controller = RecentsController(this)
        super.onSetup()
    }

    override fun showItem(item: RecentAccount) {
        BottomFragment(RecentFragment.newInstance(item.id)).show(
            baseActivity.supportFragmentManager,
            RecentFragment.TAG
        )
    }

    companion object {
        fun newInstance(
            filter: String? = null,
            isHideNoResults: Boolean = false
        ) =
            RecentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FILTER, filter)
                    putBoolean(ARG_IS_HIDE_NO_RESULTS, isHideNoResults)
                }
            }
    }
}