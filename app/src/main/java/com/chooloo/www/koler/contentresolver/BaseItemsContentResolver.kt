package com.chooloo.www.koler.contentresolver

import android.content.Context
import android.database.Cursor

abstract class BaseItemsContentResolver<ItemType>(context: Context) :
    BaseContentResolver<ArrayList<ItemType>>(context) {

    override fun convertCursorToContent(cursor: Cursor?): ArrayList<ItemType> {
        val content = ArrayList<ItemType>()
        while (cursor != null && cursor.moveToNext()) {
            content.add(convertCursorToItem(cursor))
        }
        cursor?.close()
        return content
    }

    abstract fun convertCursorToItem(cursor: Cursor): ItemType
}