package com.chooloo.www.koler.ui.contactsuggestions

import android.view.View
import com.chooloo.www.koler.ui.contacts.ContactsFragment

class ContactsSuggestionsFragment : ContactsFragment() {
    override val controller by lazy { ContactsSuggestionsController(this) }

    override fun showEmpty(isShow: Boolean) {
        binding.apply {
            empty.emptyIcon.visibility = View.GONE
            empty.emptyText.visibility = View.GONE
            itemsRecyclerView.visibility = if (isShow) View.GONE else View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = ContactsSuggestionsFragment()
    }
}