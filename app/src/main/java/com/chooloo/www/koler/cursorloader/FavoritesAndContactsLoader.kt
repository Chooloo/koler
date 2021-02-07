package com.chooloo.www.koler.cursorloader

import android.content.Context
import android.database.Cursor
import android.database.MergeCursor
import android.os.Bundle
import java.util.*

/**
 * Extends the basic ContactsCursorLoader but also adds the favourite contacts to it
 */
class FavoritesAndContactsLoader(context: Context, val phoneNumber: String?, val contactName: String?) : ContactsCursorLoader(
        context,
        phoneNumber,
        contactName
) {

    companion object {
        const val EXTRA_FAVORITE_COUNT = "favorites_count"

        protected const val FAVORITES_CONTACTS_ORDER = CONTACTS_ORDER
        protected const val FAVORITES_SELECTION = "$COLUMN_STARRED = 1"
        protected val FAVORITES_CONTACTS_PROJECTION = CONTACTS_PROJECTION
    }

    override fun loadInBackground(): Cursor? {
        val cursors: MutableList<Cursor?> = ArrayList()
        val contactsCursor = super.loadInBackground()
        val favoritesCursor = loadFavorites(phoneNumber, contactName)

        cursors.add(favoritesCursor)
        cursors.add(contactsCursor)

        return object : MergeCursor(cursors.toTypedArray()) {
            override fun getExtras(): Bundle {
                val extras = contactsCursor?.extras ?: Bundle()
                extras.putInt(EXTRA_FAVORITE_COUNT, favoritesCursor?.count ?: 0)
                return extras
            }
        }
    }

    private fun loadFavorites(phoneNumber: String?, contactName: String?): Cursor? {
        return context.contentResolver.query(
                buildUri(phoneNumber, contactName),
                FAVORITES_CONTACTS_PROJECTION,
                FAVORITES_SELECTION,
                null,
                FAVORITES_CONTACTS_ORDER)
    }
}