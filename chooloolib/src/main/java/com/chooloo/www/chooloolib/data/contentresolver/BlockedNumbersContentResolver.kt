package com.chooloo.www.chooloolib.data.contentresolver

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.BlockedNumberContract.BlockedNumbers
import com.chooloo.www.chooloolib.domain.model.record.BlockedNumberRecord
import com.chooloo.www.chooloolib.utils.SelectionBuilder
import com.chooloo.www.chooloolib.utils.getLong
import com.chooloo.www.chooloolib.utils.getString

class BlockedNumbersContentResolver(
    filter: String? = null,
    contentResolver: ContentResolver,
) : BaseContentResolver<BlockedNumberRecord>(filter, contentResolver) {

    private val selectionBuilder = SelectionBuilder().apply {
        currentFilter?.let {
            val pattern = "%$it%"
            addRaw(
                "(${BlockedNumbers.COLUMN_ORIGINAL_NUMBER} LIKE ? OR ${BlockedNumbers.COLUMN_E164_NUMBER} LIKE ?)",
                pattern,
                pattern
            )
        }
    }

    override val uri: Uri = BlockedNumbers.CONTENT_URI
    override val filterUri: Uri? = null
    override val sortOrder: String? = null
    override val selection: String? = selectionBuilder.build()
    override val selectionArgs: Array<String>? = selectionBuilder.buildArgs()
    override val projection: Array<String> = arrayOf(
        BlockedNumbers.COLUMN_ID,
        BlockedNumbers.COLUMN_ORIGINAL_NUMBER,
        BlockedNumbers.COLUMN_E164_NUMBER,
    )

    override fun convertCursorToItem(cursor: Cursor) = BlockedNumberRecord(
        id = cursor.getLong(BlockedNumbers.COLUMN_ID),
        number = cursor.getString(BlockedNumbers.COLUMN_ORIGINAL_NUMBER) ?: "",
        e164Number = cursor.getString(BlockedNumbers.COLUMN_E164_NUMBER),
    )
}
