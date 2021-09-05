package com.chooloo.www.koler.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.data.account.Recent
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.getHoursString

class RecentsAdapter(boundComponent: BoundComponentRoot) :
    ListAdapter<Recent>(boundComponent, DIFF_CALLBACK) {

    override fun onBindListItem(listItem: ListItem, item: Recent) {
        listItem.apply {
            val account = boundComponent.phoneAccountsInteractor.lookupAccount(item.number)

            titleText = account.name ?: item.number
            isCompact = boundComponent.preferencesInteractor.isCompact
            captionText = if (item.date != null) context.getHoursString(item.date) else null
            imageDrawable = ContextCompat.getDrawable(context, getCallTypeImage(item.type))

            setImageBackgroundColor(Color.TRANSPARENT)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recent>() {
            override fun areItemsTheSame(oldItem: Recent, newItem: Recent) = oldItem == newItem
            override fun areContentsTheSame(oldItem: Recent, newItem: Recent) = oldItem == newItem
        }
    }
}