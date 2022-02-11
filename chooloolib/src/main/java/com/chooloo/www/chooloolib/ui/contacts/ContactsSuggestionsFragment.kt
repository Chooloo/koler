package com.chooloo.www.chooloolib.ui.contacts

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactsSuggestionsFragment @Inject constructor() : ContactsFragment() {
    override val viewState: ContactsSuggestionsViewState by activityViewModels()


    override fun onSetup() {
        adapter.apply {
            withFavs = false
            isCompact = true
            withHeaders = false
        }
        super.onSetup()
    }

    override fun showEmpty(isShow: Boolean) {
        binding.apply {
            empty.emptyIcon.isVisible = false
            empty.emptyText.isVisible = false
            itemsScrollView.isVisible = !isShow
        }
    }
}