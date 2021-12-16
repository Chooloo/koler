package com.chooloo.www.koler.adapter

import com.chooloo.www.koler.data.account.ContactAccount
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent
import com.chooloo.www.koler.ui.widgets.listitem.ListItem

class ContactsSuggestionsAdapter(activityComponent: ActivityComponent) :
    ContactsAdapter(activityComponent) {

    override fun onBindListItem(listItem: ListItem, item: ContactAccount) {
        super.onBindListItem(listItem, item)
        listItem.isCompact = true
    }
}