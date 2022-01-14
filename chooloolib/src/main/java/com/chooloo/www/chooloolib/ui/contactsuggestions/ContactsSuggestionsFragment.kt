package com.chooloo.www.chooloolib.ui.contactsuggestions

import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment

class ContactsSuggestionsFragment : ContactsFragment() {
    override val controller by lazy { ContactsSuggestionsController(this) }

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