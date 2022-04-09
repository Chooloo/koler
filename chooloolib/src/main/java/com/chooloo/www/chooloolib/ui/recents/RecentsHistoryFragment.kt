package com.chooloo.www.chooloolib.ui.recents

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.adapter.RecentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecentsHistoryFragment : RecentsFragment() {
    override val viewState: RecentsHistoryViewState by activityViewModels()

    @Inject
    override lateinit var adapter: RecentsAdapter

    override fun onSetup() {
        super.onSetup()

        adapter.showHistory = true
    }

    companion object {
        fun newInstance(filter: String? = null) = RecentsHistoryFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FILTER, filter)
            }
        }
    }
}