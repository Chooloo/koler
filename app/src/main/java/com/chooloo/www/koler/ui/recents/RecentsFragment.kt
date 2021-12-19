package com.chooloo.www.koler.ui.recents

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.data.ListBundle
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.list.ListContract
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.ui.recent.RecentFragment
import java.io.Serializable

class RecentsFragment : ListFragment<Recent, RecentsAdapter>(), ListContract.View<Recent> {
    override val searchHint by lazy { getString(R.string.hint_search_recents) }

    override lateinit var presenter: RecentsPresenter<RecentsFragment>

    override lateinit var bottomFragment: BottomFragment<Fragment>

    override fun onSetup() {
        presenter = RecentsPresenter(this)
        super.onSetup()
    }

    override fun showItem(item: Recent) {
        bottomFragment = BottomFragment(RecentFragment.newInstance(item.id, this::hideItem))
        bottomFragment.show(
            baseActivity.supportFragmentManager,
            RecentFragment.TAG
        )
    }

    override fun hideItem() {
        bottomFragment.dismiss()
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