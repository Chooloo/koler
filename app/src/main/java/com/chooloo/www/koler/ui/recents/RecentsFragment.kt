package com.chooloo.www.koler.ui.recents

import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.livedata.RecentsProviderLiveData
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.util.runWithPermissions

class RecentsFragment : ListFragment<RecentsAdapter>(), RecentsMvpView {
    private lateinit var _presenter: RecentsPresenter<RecentsMvpView>
    private lateinit var _recentsLiveData: RecentsProviderLiveData

    companion object {
        fun newInstance(): RecentsFragment = RecentsFragment()
    }

    override fun onGetAdapter(): RecentsAdapter {
        return RecentsAdapter(_activity).apply {
            setOnRecentItemClickListener { recent -> _presenter.onRecentItemClick(recent) }
            setOnRecentItemLongClickListener { recent -> _presenter.onRecentItemLongClick(recent) }
        }
    }

    override fun onSetup() {
        super.onSetup()

        _presenter = RecentsPresenter()
        _presenter.attach(this)

        _recentsLiveData = RecentsProviderLiveData(_activity)

        showEmptyPage(false)
        showNoPermissions(false)
        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun observe() = runWithPermissions(_recentsLiveData.requiredPermissions) {
        _recentsLiveData.observe(viewLifecycleOwner, { recents -> adapter.updateRecents(recents) })
    }

    override fun openRecent(recent: Recent) {
        // TODO implement
    }
}