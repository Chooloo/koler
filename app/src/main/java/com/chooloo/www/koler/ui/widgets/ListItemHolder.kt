package com.chooloo.www.koler.ui.widgets

import android.content.Context
import android.graphics.Color
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.koler.contentresolver.RecentsContentResolver
import com.chooloo.www.koler.data.account.Contact
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.util.getHoursString
import com.chooloo.www.koler.util.initials

class ListItemHolder(val listItem: ListItem) : RecyclerView.ViewHolder(listItem) {
    constructor(context: Context) : this(ListItem(context))
}