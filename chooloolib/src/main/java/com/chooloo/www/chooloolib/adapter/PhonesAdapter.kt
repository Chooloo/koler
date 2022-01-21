package com.chooloo.www.chooloolib.adapter

import android.graphics.Color
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.ViewGroup
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.ListData
import com.chooloo.www.chooloolib.data.account.PhoneAccount
import com.chooloo.www.chooloolib.di.activitycomponent.ActivityComponent
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItemButton
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItemHolder

class PhonesAdapter(activityComponent: ActivityComponent) :
    ListAdapter<PhoneAccount>(activityComponent) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListItemHolder(ListItemButton(parent.context))


    override fun onBindListItem(listItem: ListItem, item: PhoneAccount) {
        listItem.apply {
            isPadded = false
            titleText = item.number
            captionText =
                component.strings.getString(Phone.getTypeLabelResource(item.type))

            setTitleBold(true)
            setImageBackgroundColor(Color.TRANSPARENT)
            setImageResource(R.drawable.ic_call_black_24dp)
        }
    }

    override fun convertDataToListData(data: List<PhoneAccount>) = ListData.fromPhones(data)
}