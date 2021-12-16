package com.chooloo.www.koler.adapter

import android.net.Uri
import com.chooloo.www.koler.data.account.ContactAccount
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent
import com.chooloo.www.koler.ui.list.ListData
import com.chooloo.www.koler.ui.widgets.listitem.ListItem
import com.chooloo.www.koler.util.initials

open class ContactsAdapter(activityComponent: ActivityComponent) :
    ListAdapter<ContactAccount>(activityComponent) {

    override fun onBindListItem(listItem: ListItem, item: ContactAccount) {
        listItem.apply {
            titleText = item.name
            isCompact = component.preferences.isCompact
            component.phones.getContactAccounts(item.id) { accounts ->
                captionText = accounts?.firstOrNull()?.number
            }

            setImageInitials(item.name?.initials())
            setImageUri(if (item.photoUri != null) Uri.parse(item.photoUri) else null)
        }
    }

    override fun convertDataToListData(data: List<ContactAccount>) = ListData.fromContacts(data)
}