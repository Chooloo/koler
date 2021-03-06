package com.chooloo.www.koler.ui.recents

import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.data.RecentsBundle
import com.chooloo.www.koler.livedata.RecentsProviderLiveData
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.ui.recent.RecentFragment

class RecentsFragment : ListFragment<Recent, RecentsBundle, RecentsAdapter>(),
    RecentsContract.View {
    private val _presenter by lazy { RecentsPresenter<RecentsContract.View>() }
    private val _recentsLiveData by lazy { RecentsProviderLiveData(_activity) }

    override val adapter by lazy { RecentsAdapter(_activity) }
    override val searchHint by lazy { getString(R.string.hint_search_recents) }
    override val requiredPermissions get() = _recentsLiveData.requiredPermissions
    override val noResultsMessage by lazy { getString(R.string.error_no_results_recents) }
    override val noPermissionsMessage by lazy { getString(R.string.error_no_permissions_recents) }

    companion object {
        const val ARG_FILTER = "filter"

        fun newInstance(
            isCompact: Boolean = false,
            isSearchable: Boolean = true,
            filter: String? = null
        ) =
            RecentsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_COMPACT, isCompact)
                    putBoolean(ARG_IS_SEARCHABLE, isSearchable)
                    putString(ARG_FILTER, filter)
                }
            }
    }

    override fun onSetup() {
        super.onSetup()
        _presenter.attach(this)
    }

    override fun onAttachData() {
        _recentsLiveData.observe(viewLifecycleOwner, _presenter::onRecentsChanged)
        argsSafely.getString(ARG_FILTER)?.let { applyFilter(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun openRecent(recent: Recent) {
        BottomFragment(RecentFragment.newInstance(recent.id)).show(
            _activity.supportFragmentManager,
            RecentFragment.TAG
        )
    }

    override fun applyFilter(filter: String) {
        _recentsLiveData.filter = filter
    }

    override fun onItemClick(item: Recent) {
        _presenter.onRecentItemClick(item)
    }

    override fun onItemLongClick(item: Recent) {
        _presenter.onRecentItemLongClick(item)
    }
}