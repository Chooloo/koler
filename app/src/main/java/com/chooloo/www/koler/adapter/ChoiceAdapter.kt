package com.chooloo.www.koler.adapter

import com.chooloo.www.koler.di.activitycomponent.ActivityComponent
import com.chooloo.www.koler.ui.list.ListData
import com.chooloo.www.koler.ui.widgets.listitem.ListItem

class ChoiceAdapter(component: ActivityComponent) : ListAdapter<String>(component) {
    override fun onBindListItem(listItem: ListItem, item: String) {
        listItem.apply {
            titleText = item
            imageVisibility = false
        }
    }

    override fun convertDataToListData(data: List<String>) = ListData(data)
}