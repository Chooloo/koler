package com.chooloo.www.koler.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.getHoursString

class RecentsAdapter(boundComponent: BoundComponentRoot) : ListAdapter<Recent>(boundComponent) {
    override fun onBindListItem(listItem: ListItem, item: Recent) {
        listItem.apply {
            boundComponent.phoneAccountsInteractor.lookupAccount(item.number) {
                titleText = it.name ?: item.number
            }

            isCompact = boundComponent.preferencesInteractor.isCompact
            captionText = if (item.date != null) context.getHoursString(item.date) else null
            imageDrawable = ContextCompat.getDrawable(context, getCallTypeImage(item.type))

            setImageBackgroundColor(Color.TRANSPARENT)
        }
    }
}