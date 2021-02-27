package com.chooloo.www.koler.contentresolver

import android.content.Context
import android.provider.ContactsContract

class FavoritesContentResolver(
    context: Context
) : ContactsContentResolver(context) {
    override fun onGetSelection() = SelectionBuilder()
        .addString(super.onGetSelection())
        .addSelection(ContactsContract.Contacts.STARRED, 1)
        .build()
}