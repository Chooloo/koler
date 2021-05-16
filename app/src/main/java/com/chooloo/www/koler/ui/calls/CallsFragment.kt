package com.chooloo.www.koler.ui.calls

import android.os.Bundle
import com.chooloo.www.koler.R
import com.chooloo.www.koler.adapter.CallsAdapter
import com.chooloo.www.koler.data.CallsBundle
import com.chooloo.www.koler.ui.list.ListFragment
import com.chooloo.www.koler.util.call.CallItem

class CallsFragment : ListFragment<CallItem, CallsBundle, CallsAdapter>(), CallsContract.View {
    private val _presenter by lazy { CallsPresenter<CallsFragment>() }

    override val adapter by lazy { CallsAdapter() }
    override val noResultsMessage by lazy { getString(R.string.error_no_results_calls) }

    companion object {
        fun newInstance(isHideNoResults: Boolean = false) = CallsFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_IS_HIDE_NO_RESULTS, isHideNoResults)
            }
        }
    }

    override fun onSetup() {
        super.onSetup()
        _presenter.attach(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
    }

    override fun updateCallItem(callItem: CallItem) {
        adapter.updateCallItem(callItem)
    }

    override fun updateCallItems(callItems: ArrayList<CallItem>) {
        callItems.forEach { updateCallItem(it) }
    }
}