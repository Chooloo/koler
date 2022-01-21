package com.chooloo.www.chooloolib.adapter

import android.net.Uri
import com.chooloo.www.chooloolib.data.ListData
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.di.activitycomponent.ActivityComponent
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.chooloo.www.chooloolib.util.initials

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

    override fun convertDataToListData(data: List<ContactAccount>) =
        ListData.fromContacts(data, true)
}