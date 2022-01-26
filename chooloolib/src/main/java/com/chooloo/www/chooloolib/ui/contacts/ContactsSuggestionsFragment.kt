package com.chooloo.www.chooloolib.ui.contacts

import androidx.core.view.isVisible

class ContactsSuggestionsFragment : ContactsFragment() {
    override fun showEmpty(isShow: Boolean) {
        binding.apply {
            empty.emptyIcon.isVisible = false
            empty.emptyText.isVisible = false
            itemsScrollView.isVisible = !isShow
        }
    }

    companion object {
        fun newInstance() = ContactsSuggestionsFragment()
    }
}