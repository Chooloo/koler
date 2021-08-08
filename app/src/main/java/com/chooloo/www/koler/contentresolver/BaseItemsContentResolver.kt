package com.chooloo.www.koler.contentresolver

import android.content.Context
import android.database.Cursor

abstract class BaseItemsContentResolver<DataType>(context: Context) :
    BaseContentResolver<ArrayList<DataType>>(context) {

    override fun convertCursorToContent(cursor: Cursor?): ArrayList<DataType> {
        val content = ArrayList<DataType>()
        while (cursor != null && cursor.moveToNext()) {
            content.add(convertCursorToItem(cursor))
        }
        cursor?.close()
        return content
    }

    abstract fun convertCursorToItem(cursor: Cursor): DataType
}