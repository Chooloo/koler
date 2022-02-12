package com.chooloo.www.chooloolib.ui.recents

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentsHistoryFragment : RecentsFragment() {
    override val viewState: RecentsHistoryViewState by activityViewModels()

    companion object {
        fun newInstance(filter: String? = null) = RecentsHistoryFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FILTER, filter)
            }
        }
    }
}