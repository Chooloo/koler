package com.chooloo.www.koler.adapter

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.initials

class ContactsAdapter(boundComponent: BoundComponentRoot) :
    ListAdapter<Contact>(boundComponent, DIFF_CALLBACK) {
    override fun onBindListItem(listItem: ListItem, item: Contact) {
        listItem.apply {
            titleText = item.name

            setImageInitials(item.name?.initials())
            setImageUri(if (item.photoUri != null) Uri.parse(item.photoUri) else null)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact) = oldItem == newItem
            override fun areContentsTheSame(oldItem: Contact, newItem: Contact) = oldItem == newItem
        }
    }
}