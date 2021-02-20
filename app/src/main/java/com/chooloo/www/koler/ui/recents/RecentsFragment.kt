package com.chooloo.www.koler.ui.recents

import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.livedata.RecentsProviderLiveData
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.ui.recent.RecentBottomDialogFragment
import com.chooloo.www.koler.util.permissions.runWithPermissions
import com.chooloo.www.koler.viewmodel.SearchViewModel

class RecentsFragment : ListFragment<RecentsAdapter>(), RecentsMvpView {

    private val _searchViewModel by lazy { ViewModelProvider(requireActivity()).get(SearchViewModel::class.java) }
    private val _recentsLiveData by lazy { RecentsProviderLiveData(_activity) }
    private val _presenter by lazy { RecentsPresenter<RecentsMvpView>() }

    companion object {
        fun newInstance(): RecentsFragment = RecentsFragment()
    }

    override fun onGetAdapter() = RecentsAdapter(_activity).apply {
        setOnRecentItemClickListener { recent -> _presenter.onRecentItemClick(recent) }
        setOnRecentItemLongClickListener { recent -> _presenter.onRecentItemLongClick(recent) }
    }

    override fun onSetup() {
        super.onSetup()

        _presenter.attach(this)

        _searchViewModel.apply {
            number.observe(viewLifecycleOwner) { _recentsLiveData.setFilter(it) }
            text.observe(viewLifecycleOwner) { _recentsLiveData.setFilter(it) }
        }

        showEmptyPage(false)
        showNoPermissions(false)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun observe() = runWithPermissions(_recentsLiveData.requiredPermissions) {
        _recentsLiveData.observe(viewLifecycleOwner) { listAdapter.updateRecents(it) }
    }

    override fun openRecent(recent: Recent) {
        RecentBottomDialogFragment.newInstance(recent.id)
            .show(_activity.supportFragmentManager, RecentBottomDialogFragment.TAG)
    }
}