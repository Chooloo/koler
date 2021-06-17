package com.chooloo.www.koler.contentresolver

import android.content.Context
import android.database.Cursor

abstract class BaseItemsContentResolver<DataType>(context: Context) :
    BaseContentResolver<ArrayList<DataType>>(context) {
    override fun convertCursorToContent(cursor: Cursor?): ArrayList<DataType> {
        return ArrayList<DataType>().apply {
            while (cursor != null && cursor.moveToNext()) cursor.apply {
                add(convertCursorToItem(cursor))
            }
        }.also {
            cursor?.close()
        }
    }

    abstract fun convertCursorToItem(cursor: Cursor): DataType
}