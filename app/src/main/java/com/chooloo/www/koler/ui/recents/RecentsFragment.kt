package com.chooloo.www.koler.ui.recents

import android.Manifest.permission.READ_CONTACTS
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.data.RecentsBundle
import com.chooloo.www.koler.livedata.RecentsProviderLiveData
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.ui.recent.RecentFragment
import com.chooloo.www.koler.util.permissions.runWithPermissions
import com.chooloo.www.koler.viewmodel.SearchViewModel

class RecentsFragment : ListFragment<RecentsAdapter>(), RecentsContract.View {
    private val _presenter by lazy { RecentsPresenter<RecentsContract.View>() }
    private val _recentsLiveData by lazy { RecentsProviderLiveData(_activity) }
    private val _searchViewModel by lazy { ViewModelProvider(requireActivity()).get(SearchViewModel::class.java) }

    companion object {
        fun newInstance() = RecentsFragment()
    }

    override fun onGetAdapter() = RecentsAdapter(_activity).apply {
        setOnItemClickListener(_presenter::onRecentItemClick)
        setOnItemLongClickListener(_presenter::onRecentItemLongClick)
    }

    override fun onSetup() {
        super.onSetup()

        _presenter.attach(this)

        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun observe() = runWithPermissions(
        permissions = _recentsLiveData.requiredPermissions + READ_CONTACTS,
        grantedCallback = {
            _recentsLiveData.observe(viewLifecycleOwner, _presenter::onRecentsChanged)
            _searchViewModel.apply {
                number.observe(viewLifecycleOwner, _presenter::onDialpadNumberChanged)
                text.observe(viewLifecycleOwner, _presenter::onSearchTextChanged)
            }
            emptyMessage = getString(R.string.error_no_results)
        },
        blockedCallback = _presenter::onPermissionsBlocked
    )

    override fun openRecent(recent: Recent) {
        BottomFragment(RecentFragment.newInstance(recent.id)).show(
            _activity.supportFragmentManager,
            RecentFragment.TAG
        )
    }

    override fun updateRecents(recentsBundle: RecentsBundle) {
        listAdapter.data = recentsBundle.listBundleByDates
    }

    override fun setRecentsFilter(filter: String?) {
        _recentsLiveData.setFilter(filter)
    }
}