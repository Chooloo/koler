package com.chooloo.www.chooloolib.ui.contactsuggestions

import androidx.core.view.isVisible
import com.chooloo.www.chooloolib.ui.contacts.ContactsFragment
import javax.inject.Inject

class ContactsSuggestionsFragment : ContactsFragment(), ContactsSuggestionsContract.View {
    @Inject
    override lateinit var controller: ContactsSuggestionsContract.Controller<ContactsSuggestionsFragment>

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