package com.chooloo.www.chooloolib.ui.contacts

import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactsSuggestionsFragment @Inject constructor() : ContactsFragment() {
    override fun showEmpty(isShow: Boolean) {
        binding.apply {
            empty.emptyIcon.isVisible = false
            empty.emptyText.isVisible = false
            itemsScrollView.isVisible = !isShow
        }
    }
}