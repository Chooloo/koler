package com.chooloo.www.koler.adapter

import PhoneAccount
import com.chooloo.www.koler.ui.widgets.ListItem

class PhonesAdapter : ListAdapter<PhoneAccount>() {
    override fun onBindListItem(listItem: ListItem, item: PhoneAccount) {
        listItem.apply {
            titleText = item.number
        }
    }
}