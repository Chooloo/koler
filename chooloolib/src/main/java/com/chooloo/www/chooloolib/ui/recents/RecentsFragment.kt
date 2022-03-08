package com.chooloo.www.chooloolib.ui.recents

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.adapter.RecentsAdapter
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.model.RecentAccount
import com.chooloo.www.chooloolib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class RecentsFragment @Inject constructor() : ListFragment<RecentAccount, RecentsViewState>() {
    @Inject lateinit var prompts: PromptsInteractor
    @Inject override lateinit var adapter: RecentsAdapter
    @Inject lateinit var fragmentFactory: FragmentFactory

    override val viewState: RecentsViewState by activityViewModels()


    override fun onSetup() {
        super.onSetup()

        viewState.showRecentEvent.observe(this@RecentsFragment) { ev ->
            ev.ifNew?.let {
                prompts.showFragment(fragmentFactory.getRecentFragment(it))
            }
        }
    }

    companion object {
        fun newInstance(filter: String? = null) = RecentsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_FILTER, filter)
            }
        }
    }
}