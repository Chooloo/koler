package com.chooloo.www.callmanager.ui.widgets

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.callmanager.ui.widgets.ListItem

class ListItemHolder(
        val listItem: ListItem
) : RecyclerView.ViewHolder(listItem) {

    constructor(context: Context) : this(ListItem(context))
}