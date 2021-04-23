package com.chooloo.www.koler.adapter

import android.net.Uri
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.Contact
import com.chooloo.www.koler.ui.widgets.ListItem

class ContactsAdapter : ListAdapter<Contact>() {
    override fun onBindListItem(listItem: ListItem, item: Contact) {
        listItem.apply {
            titleText = item.name
            setLeftPadding(resources.getDimension(R.dimen.default_spacing_big).toInt())
            setImageUri(if (item.photoUri != null) Uri.parse(item.photoUri) else null)
        }
    }
}