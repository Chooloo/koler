package com.chooloo.www.koler.ui.recents

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.data.RecentsBundle
import com.chooloo.www.koler.livedata.RecentsProviderLiveData
import com.chooloo.www.koler.ui.base.BottomFragment
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.ui.recent.RecentFragment
import com.chooloo.www.koler.viewmodel.SearchViewModel

class RecentsFragment : ListFragment<Recent, RecentsAdapter>(), RecentsContract.View {
    private var _onRecentsChangedListener: (RecentsBundle) -> Unit? = {}
    private val _presenter by lazy { RecentsPresenter<RecentsContract.View>() }
    private val _recentsLiveData by lazy { RecentsProviderLiveData(_activity) }
    private val _searchViewModel by lazy { ViewModelProvider(requireActivity()).get(SearchViewModel::class.java) }

    //region list args
    override val noResultsMessage by lazy { getString(R.string.error_no_results_recents) }
    override val requiredPermissions get() = _recentsLiveData.requiredPermissions
    override val noPermissionsMessage by lazy { getString(R.string.error_no_permissions_recents) }
    override val adapter by lazy {
        RecentsAdapter(_activity).apply {
            setOnItemClickListener(_presenter::onRecentItemClick)
            setOnItemLongClickListener(_presenter::onRecentItemLongClick)
        }
    }
    //endregion

    companion object {
        const val ARG_OBSERVE_SEARCH = "observe_search"

        fun newInstance(isCompact: Boolean = false, observeSearch: Boolean = false) =
            RecentsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_COMPACT, isCompact)
                    putBoolean(ARG_OBSERVE_SEARCH, observeSearch)
                }
            }
    }

    override fun onSetup() {
        super.onSetup()
        _presenter.attach(this)
    }

    override fun onAttachData() {
        _recentsLiveData.observe(viewLifecycleOwner) {
            _presenter.onRecentsChanged(it)
            _onRecentsChangedListener.invoke(it)
        }
        if (argsSafely.getBoolean(ARG_OBSERVE_SEARCH)) {
            _searchViewModel.recentsText.observe(viewLifecycleOwner, ::setRecentsFilter)
        }
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

    override fun updateRecents(recentsBundle: RecentsBundle) {
        adapter.data = recentsBundle.listBundleByDates
    }

    override fun setRecentsFilter(filter: String?) {
        _recentsLiveData.filter = filter
    }

    fun setOnRecentsChangedListener(onRecentsChangedListener: (RecentsBundle) -> Unit? = {}) {
        _onRecentsChangedListener = onRecentsChangedListener
    }
}