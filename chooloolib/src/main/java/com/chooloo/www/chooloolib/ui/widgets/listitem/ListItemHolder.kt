package com.chooloo.www.chooloolib.ui.widgets.listitem

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

class ListItemHolder(val listItem: ListItem) : RecyclerView.ViewHolder(listItem) {
    constructor(context: Context) : this(ListItem(context))
}