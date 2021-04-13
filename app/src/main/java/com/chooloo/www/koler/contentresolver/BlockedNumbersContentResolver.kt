package com.chooloo.www.koler.contentresolver

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BlockedNumberContract.BlockedNumbers

class BlockedNumbersContentResolver(
    context: Context,
    number: String? = null
) : BaseContentResolver<Array<String>>(context) {
    override val uri = BlockedNumbers.CONTENT_URI

    override val filterUri: Uri? = null

    override val selection = SelectionBuilder()
        .addSelection(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        .build()

    override val sortOrder: String? = null

    override val projection = arrayOf(
        BlockedNumbers.COLUMN_ID,
        BlockedNumbers.COLUMN_E164_NUMBER,
        BlockedNumbers.COLUMN_ORIGINAL_NUMBER,
    )

    override val selectionArgs: Array<String>? = null

    override fun convertCursorToContent(cursor: Cursor?): Array<String> {
        val numbers = arrayListOf<String>()
        cursor?.let {
            while (cursor.moveToNext()) {
                numbers.add(it.getString(it.getColumnIndex(BlockedNumbers.COLUMN_ORIGINAL_NUMBER)))
            }
        }
        return numbers.toTypedArray()
    }
}