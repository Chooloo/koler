package com.chooloo.www.koler.adapter

import android.net.Uri
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot
import com.chooloo.www.koler.ui.widgets.listitem.ListItem
import com.chooloo.www.koler.util.initials

class ContactsAdapter(boundComponent: BoundComponentRoot) :
    ListAdapter<Contact>(boundComponent) {
    override fun onBindListItem(listItem: ListItem, item: Contact) {
        listItem.apply {
            titleText = item.name
            boundComponent.phoneAccountsInteractor.getContactAccounts(item.id) { account ->
                captionText = account?.firstOrNull()?.number
            }

            setImageInitials(item.name?.initials())
            setImageUri(if (item.photoUri != null) Uri.parse(item.photoUri) else null)
        }
    }
}