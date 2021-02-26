package com.chooloo.www.koler.adapter

import android.net.Uri
import com.chooloo.www.koler.entity.Contact
import com.chooloo.www.koler.ui.widgets.ListItem

class ContactsAdapter : ListAdapter<Contact>() {
    override fun onBindListItem(listItem: ListItem, item: Contact) {
        listItem.apply {
            bigText = item.name
            item.photoUri?.let { setImageUri(Uri.parse(it)) }
        }
    }
}