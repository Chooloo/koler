package com.chooloo.www.chooloolib.adapter

import android.graphics.Color
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.model.RecentAccount
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.chooloo.www.chooloolib.util.getHoursString
import com.chooloo.www.chooloolib.util.initials
import javax.inject.Inject

class RecentsAdapter @Inject constructor(
    animations: AnimationsInteractor,
    private val phones: PhonesInteractor,
    private val strings: StringsInteractor,
    private val recents: RecentsInteractor,
    private val preferences: PreferencesInteractor
) : ListAdapter<RecentAccount>(animations) {

    override fun onBindListItem(listItem: ListItem, item: RecentAccount) {
        listItem.apply {
            isCompact = preferences.isCompact
            captionText = if (item.date != null) context.getHoursString(item.date) else null
            phones.lookupAccount(item.number) {
                titleText = it?.name ?: item.number
                it?.let {
                    captionText =
                        "$captionText · ${Phone.getTypeLabel(resources, it.type, it.label)} ·"
                    setImageInitials(it.name?.initials())
                    setImageUri(if (it.photoUri != null) Uri.parse(it.photoUri) else null)
                }
            }

            setImageBackgroundColor(Color.TRANSPARENT)
            setCaptionImageRes(recents.getCallTypeImage(item.type))
        }
    }

    override fun convertDataToListData(data: List<RecentAccount>) = ListData.fromRecents(data)
}