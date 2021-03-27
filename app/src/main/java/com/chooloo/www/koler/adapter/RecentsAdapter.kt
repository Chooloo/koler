package com.chooloo.www.koler.adapter

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.R
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.getHoursString
import com.chooloo.www.koler.util.lookupContact

class RecentsAdapter(
    private val context: Context
) : ListAdapter<Recent>() {
    override fun onBindListItem(listItem: ListItem, item: Recent) {
        val contact = context.lookupContact(item.number)
        listItem.apply {
            titleText = contact.name ?: item.number
            captionText = if (item.date != null) getHoursString(item.date) else null
            imageDrawable = ContextCompat.getDrawable(context, getCallTypeImage(item.type))
            personStartPadding = resources.getDimensionPixelSize(R.dimen.default_spacing_big)

            setImageBackgroundColor(Color.TRANSPARENT)
        }
    }
}