package com.chooloo.www.chooloolib.ui.recents

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.chooloo.www.chooloolib.adapter.RecentsAdapter
import com.chooloo.www.chooloolib.di.factory.fragment.FragmentFactory
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.prompt.PromptsInteractor
import com.chooloo.www.chooloolib.model.RecentAccount
import com.chooloo.www.chooloolib.ui.list.ListFragment
import com.chooloo.www.chooloolib.ui.recentshistory.RecentsHistoryViewState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class RecentsFragment @Inject constructor() : ListFragment<RecentAccount, RecentsViewState>() {
    @Inject override lateinit var adapter: RecentsAdapter
    override val viewState: RecentsViewState by activityViewModels()

    @Inject lateinit var prompts: PromptsInteractor
    @Inject lateinit var fragmentFactory: FragmentFactory
    @Inject lateinit var preferences: PreferencesInteractor
    private val recentsHistoryViewState: RecentsHistoryViewState by activityViewModels()


    override fun onSetup() {
        super.onSetup()
        adapter.groupSimilar = args.getBoolean(ARG_IS_GROUPED, preferences.isGroupRecents)

        viewState.showRecentEvent.observe(this@RecentsFragment) { ev ->
            ev.ifNew?.let {
                if (it.groupCount > 1) {
                    prompts.showFragment(fragmentFactory.getRecentsHistoryFragment().apply {
                        arguments = arguments ?: Bundle()
                        requireArguments().putBoolean(ARG_OBSERVE, false)
                    })
                    recentsHistoryViewState.onItemsChanged(it.groupAccounts)
                } else {
                    prompts.showFragment(fragmentFactory.getRecentFragment(it.id))
                }
            }
        }
    }

    companion object {
        private const val ARG_IS_GROUPED = "is_grouped"

        fun newInstance(filter: String? = null, isGrouped: Boolean? = null) =
            RecentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FILTER, filter)
                    isGrouped?.let { putBoolean(ARG_IS_GROUPED, it) }
                }
            }
    }
}