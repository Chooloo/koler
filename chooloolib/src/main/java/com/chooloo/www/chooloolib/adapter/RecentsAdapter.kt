package com.chooloo.www.chooloolib.adapter

import android.graphics.Color
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.data.ListData
import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.di.activitycomponent.ActivityComponent
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.chooloo.www.chooloolib.util.getHoursString

class RecentsAdapter(activityComponent: ActivityComponent) :
    ListAdapter<RecentAccount>(activityComponent) {

    override fun onBindListItem(listItem: ListItem, item: RecentAccount) {
        listItem.apply {
            isCompact = component.preferences.isCompact
            captionText = if (item.date != null) context.getHoursString(item.date!!) else null
            component.phones.lookupAccount(item.number) {
                titleText = it?.name ?: item.number
                it?.let {
                    captionText = "$captionText · ${
                        component.strings.getString(Phone.getTypeLabelResource(it.type))
                    }"
                }
            }

            setImageBackgroundColor(Color.TRANSPARENT)
            setImageResource(component.recents.getCallTypeImage(item.type))
        }
    }

    override fun convertDataToListData(data: List<RecentAccount>) = ListData.fromRecents(data)
}