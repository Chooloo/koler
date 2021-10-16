package com.chooloo.www.koler.contentresolver

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BlockedNumberContract.BlockedNumbers
import com.chooloo.www.koler.util.SelectionBuilder

class BlockedNumbersContentResolver(context: Context, number: String? = null) :
    BaseItemsContentResolver<String>(context) {

    override val uri: Uri = URI
    override val filterUri: Uri? = null
    override val sortOrder: String? = null
    override val selectionArgs: Array<String>? = null
    override val projection: Array<String> = PROJECTION
    override val selection: String by lazy {
        SelectionBuilder()
            .addSelection(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
            .build()
    }

    override fun convertCursorToItem(cursor: Cursor): String =
        cursor.getString(cursor.getColumnIndex(BlockedNumbers.COLUMN_ORIGINAL_NUMBER))

    companion object {
        val URI: Uri = BlockedNumbers.CONTENT_URI;
        val PROJECTION: Array<String> = arrayOf(
            BlockedNumbers.COLUMN_ID,
            BlockedNumbers.COLUMN_E164_NUMBER,
            BlockedNumbers.COLUMN_ORIGINAL_NUMBER
        )
    }
}