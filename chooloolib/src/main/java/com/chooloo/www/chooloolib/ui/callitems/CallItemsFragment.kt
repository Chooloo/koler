package com.chooloo.www.chooloolib.ui.callitems

import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.adapter.CallItemsAdapter
import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.chooloolib.ui.list.ListFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CallItemsFragment @Inject constructor() : ListFragment<Call, CallItemsAdapter>(),
    CallItemsContract.View {
    override lateinit var controller: CallItemsContract.Controller

    override fun onSetup() {
        super.onSetup()
        controller = controllerFactory.getCallItemsController(this)
    }

    override fun showEmpty(isShow: Boolean) {
        binding.apply {
            empty.emptyIcon.isVisible = false
            empty.emptyText.isVisible = false
            itemsScrollView.isVisible = !isShow
        }
    }
}