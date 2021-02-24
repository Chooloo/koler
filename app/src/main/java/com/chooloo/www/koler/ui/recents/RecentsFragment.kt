package com.chooloo.www.koler.ui.recents

import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.livedata.RecentsProviderLiveData
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.ui.recent.RecentFragment
import com.chooloo.www.koler.util.permissions.runWithPermissions
import com.chooloo.www.koler.viewmodel.SearchViewModel

class RecentsFragment : ListFragment<RecentsAdapter>(), RecentsMvpView {

    companion object {
        fun newInstance() = RecentsFragment()
    }

    private val _searchViewModel by lazy { ViewModelProvider(requireActivity()).get(SearchViewModel::class.java) }
    private val _recentsLiveData by lazy { RecentsProviderLiveData(_activity) }
    private val _presenter by lazy { RecentsPresenter<RecentsMvpView>() }

    override fun onGetAdapter() = RecentsAdapter(_activity).apply {
        setOnRecentItemClickListener(_presenter::onRecentItemClick)
        setOnRecentItemLongClickListener(_presenter::onRecentItemLongClick)
    }

    override fun onSetup() {
        super.onSetup()

        _presenter.attach(this)

        showEmptyPage(false)
        showNoPermissions(false)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun observe() = runWithPermissions(_recentsLiveData.requiredPermissions, {
        _recentsLiveData.observe(viewLifecycleOwner, listAdapter::updateRecents)
        _searchViewModel.apply {
            number.observe(viewLifecycleOwner, _recentsLiveData::setFilter)
            text.observe(viewLifecycleOwner, _recentsLiveData::setFilter)
        }
        showNoPermissions(false)
    }, blockedCallback = { _presenter.onPermissionsBlocked() })

    override fun openRecent(recent: Recent) {
        BottomFragment.newInstance(RecentFragment.newInstance(recent.id))
            .show(_activity.supportFragmentManager, RecentFragment.TAG)
    }
}