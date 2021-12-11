package com.chooloo.www.koler.adapter

import android.net.Uri
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent
import com.chooloo.www.koler.ui.widgets.listitem.ListItem
import com.chooloo.www.koler.util.initials

class ContactsAdapter(activityComponent: ActivityComponent) :
    ListAdapter<Contact>(activityComponent) {
    override fun onBindListItem(listItem: ListItem, item: Contact) {
        listItem.apply {
            titleText = item.name
            activityComponent.phoneAccountsInteractor.getContactAccounts(item.id) { account ->
                captionText = account?.firstOrNull()?.number
            }

            setImageInitials(item.name?.initials())
            setImageUri(if (item.photoUri != null) Uri.parse(item.photoUri) else null)
        }
    }
}