package com.chooloo.www.koler.adapter

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.KolerApp
import com.chooloo.www.koler.contentresolver.RecentsContentResolver.Companion.getCallTypeImage
import com.chooloo.www.koler.data.Recent
import com.chooloo.www.koler.interactor.phoneaccounts.PhoneAccountsInteractor
import com.chooloo.www.koler.interactor.preferences.PreferencesInteractor
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.getHoursString

class RecentsAdapter(
    private val preferencesInteractor: PreferencesInteractor,
    private val phoneAccountsInteractor: PhoneAccountsInteractor
) : ListAdapter<Recent>() {
    override fun onBindListItem(listItem: ListItem, item: Recent) {
        val contact = phoneAccountsInteractor.lookupAccount(item.number)

        listItem.apply {
            isCompact = preferencesInteractor.isCompact
            titleText = contact?.name ?: item.number
            captionText = if (item.date != null) context.getHoursString(item.date) else null
            imageDrawable = ContextCompat.getDrawable(context, getCallTypeImage(item.type))

            setImageBackgroundColor(Color.TRANSPARENT)
        }
    }
}