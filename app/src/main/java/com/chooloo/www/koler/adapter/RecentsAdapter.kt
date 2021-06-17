package com.chooloo.www.koler.adapter

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.getHoursString
import com.chooloo.www.koler.util.lookupContactNumber
import com.chooloo.www.koler.util.preferences.KolerPreferences

class RecentsAdapter(
    private val _context: Context
) : ListAdapter<Recent>() {
    private val _prefIsCompact by lazy { _kolerPreferences.compact }
    private val _kolerPreferences by lazy { KolerPreferences(_context) }

    override fun onBindListItem(listItem: ListItem, item: Recent) {
        val contact = _context.lookupContactNumber(item.number)

        listItem.apply {
            isCompact = _prefIsCompact
            titleText = contact?.name ?: item.number
            captionText = if (item.date != null) context.getHoursString(item.date) else null
            imageDrawable = ContextCompat.getDrawable(context, getCallTypeImage(item.type))

            setImageBackgroundColor(Color.TRANSPARENT)
        }
    }
}