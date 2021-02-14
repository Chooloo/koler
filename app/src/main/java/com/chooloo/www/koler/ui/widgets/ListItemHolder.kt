package com.chooloo.www.koler.ui.widgets

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

class ListItemHolder(
        val listItem: ListItem
) : RecyclerView.ViewHolder(listItem) {

        constructor(context: Context) : this(ListItem(context))
}