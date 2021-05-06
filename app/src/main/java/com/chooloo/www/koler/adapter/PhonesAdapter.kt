package com.chooloo.www.koler.adapter

import PhoneAccount
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.isNumberBlocked

class PhonesAdapter : ListAdapter<PhoneAccount>() {
    override fun onBindListItem(listItem: ListItem, item: PhoneAccount) {
        listItem.apply {
            titleText = item.number
            imageVisibility = false
            
            if (context.isNumberBlocked(item.number)) {
                imageDrawable = ContextCompat.getDrawable(context, R.drawable.round_block_black_24)
            }
        }
    }
}