package com.chooloo.www.chooloolib.data.source

import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull

class CursorRow(private val cursor: Cursor) {
    fun string(column: String): String? = cursor.getStringOrNull(index(column))

    fun long(column: String): Long = cursor.getLongOrNull(index(column)) ?: 0L

    fun int(column: String): Int = cursor.getIntOrNull(index(column)) ?: 0

    private fun index(column: String): Int = cursor.getColumnIndexOrThrow(column)
}
