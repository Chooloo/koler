package com.chooloo.www.koler.adapter

import android.net.Uri
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.initials

class   ContactsAdapter : ListAdapter<Contact>() {
    override fun onBindListItem(listItem: ListItem, item: Contact) {
        listItem.apply {
            titleText = item.name

            setImageInitials(item.name?.initials())
            setImageUri(if (item.photoUri != null) Uri.parse(item.photoUri) else null)
        }
    }
}