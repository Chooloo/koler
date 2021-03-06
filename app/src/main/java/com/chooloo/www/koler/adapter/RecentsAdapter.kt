package com.chooloo.www.koler.adapter

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.getHoursString
import com.chooloo.www.koler.util.lookupContact
import com.chooloo.www.koler.util.preferences.KolerPreferences

class RecentsAdapter(
    private val context: Context
) : ListAdapter<Recent>() {
    override fun onBindListItem(listItem: ListItem, item: Recent) {
        val contact = context.lookupContact(item.number)
        listItem.apply {
            titleText = contact?.name ?: item.number
            isCompact = KolerPreferences(context).isCompact
            captionText = if (item.date != null) getHoursString(context, item.date) else null
            imageDrawable = ContextCompat.getDrawable(context, getCallTypeImage(item.type))

            setImageBackgroundColor(Color.TRANSPARENT)
        }
    }
}