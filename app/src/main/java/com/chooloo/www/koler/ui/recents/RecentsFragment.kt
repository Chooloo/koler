package com.chooloo.www.koler.ui.recents

import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.ui.recent.RecentFragment

class RecentsFragment : ListFragment<Recent, RecentsAdapter>(), ListContract.View<Recent> {
    override val searchHint by lazy { getString(R.string.hint_search_recents) }
    override lateinit var presenter: RecentsPresenter<RecentsFragment>
    override val adapter by lazy {
        RecentsAdapter(boundComponent.preferencesInteractor, boundComponent.phoneAccountsInteractor)
    }


    override fun onSetup() {
        presenter = RecentsPresenter(this)
        super.onSetup()
    }

    override fun updateData(dataList: ArrayList<Recent>) {
        adapter.data = ListBundle.fromRecents(dataList)
    }

    override fun showItem(item: Recent) {
        BottomFragment(RecentFragment.newInstance(item.id)).show(
            baseActivity.supportFragmentManager,
            RecentFragment.TAG
        )
    }


    companion object {
        fun newInstance(
            filter: String? = null,
            isCompact: Boolean = false,
            isSearchable: Boolean = true,
            isHideNoResults: Boolean = false
        ) =
            RecentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FILTER, filter)
                    putBoolean(ARG_IS_COMPACT, isCompact)
                    putBoolean(ARG_IS_SEARCHABLE, isSearchable)
                    putBoolean(ARG_IS_HIDE_NO_RESULTS, isHideNoResults)
                }
            }
    }
}