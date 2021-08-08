package com.chooloo.www.koler.adapter

import android.view.MenuItem
import com.chooloo.www.koler.ui.widgets.ListItem

class MenuAdapter : ListAdapter<MenuItem>() {
    override fun onBindListItem(listItem: ListItem, item: MenuItem) {
        listItem.apply {
            imageDrawable = item.icon
            titleText = item.title.toString()
        }
    }
}