package com.chooloo.www.chooloolib.adapter

import com.chooloo.www.chooloolib.data.ListData
import com.chooloo.www.chooloolib.data.account.ContactAccount
import com.chooloo.www.chooloolib.di.activitycomponent.ActivityComponent
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem

class ContactsSuggestionsAdapter(activityComponent: ActivityComponent) :
    ContactsAdapter(activityComponent) {

    override fun onBindListItem(listItem: ListItem, item: ContactAccount) {
        super.onBindListItem(listItem, item)
        listItem.isCompact = true
    }

    override fun convertDataToListData(data: List<ContactAccount>) = ListData.fromContacts(data)
}