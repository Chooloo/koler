package com.chooloo.www.koler.ui.recents

import androidx.lifecycle.ViewModelProvider
import com.chooloo.www.koler.adapter.RecentsAdapter
import com.chooloo.www.koler.entity.Recent
import com.chooloo.www.koler.livedata.RecentsLiveData
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.util.runWithPermissions
import com.chooloo.www.koler.viewmodel.data.DataViewModel
import com.chooloo.www.koler.viewmodel.data.DataViewModelFactory

class RecentsFragment : ListFragment<RecentsAdapter>(), RecentsMvpView {
    private lateinit var _presenter: RecentsPresenter<RecentsMvpView>
    private lateinit var _recentsLiveData: RecentsLiveData

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

        _recentsLiveData = ViewModelProvider(this, DataViewModelFactory(_activity)).get(DataViewModel::class.java).recents

        observe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _presenter.detach()
    }

    override fun observe() = runWithPermissions(RecentsLiveData.REQUIRED_PERMISSION, callback = {
        _recentsLiveData.observe(viewLifecycleOwner, { recents -> adapter.updateRecents(recents) })
    })

    override fun openRecent(recent: Recent) {
        // TODO implement
    }
}