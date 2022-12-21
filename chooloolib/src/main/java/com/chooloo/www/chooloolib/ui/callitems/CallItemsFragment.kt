package com.chooloo.www.chooloolib.ui.callitems

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.chooloo.www.chooloolib.adapter.CallItemsAdapter
import com.chooloo.www.chooloolib.data.model.Call
import com.chooloo.www.chooloolib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallItemsFragment @Inject constructor() : ListFragment<Call, CallItemsViewState>() {
    override val viewState: CallItemsViewState by viewModels()

    @Inject override lateinit var adapter: CallItemsAdapter


    override fun showEmpty(isShow: Boolean) {
        binding.apply {
            empty.emptyIcon.isVisible = false
            empty.emptyText.isVisible = false
            itemsScrollView.isVisible = !isShow
        }
    }
}