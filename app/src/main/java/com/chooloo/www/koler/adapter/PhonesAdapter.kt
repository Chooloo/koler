package com.chooloo.www.koler.adapter

import ContactsManager
import PhoneAccount
import android.content.Context
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.widgets.ListItem

class PhonesAdapter(private val _context: Context) : ListAdapter<PhoneAccount>() {
    private val _contactsManager by lazy { ContactsManager(_context) }
    private val _blockedDrawable by lazy {
        ContextCompat.getDrawable(_context, R.drawable.round_block_black_24)
    }

    override fun onBindListItem(listItem: ListItem, item: PhoneAccount) {
        listItem.apply {
            titleText = item.number
            imageVisibility = false

            if (_contactsManager.queryIsNumberBlocked(item.number)) {
                imageDrawable = _blockedDrawable
            }
        }
    }
}