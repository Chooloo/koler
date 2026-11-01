package com.chooloo.www.chooloolib.utils

import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull

fun Cursor.getString(column: String): String? =
    getStringOrNull(getColumnIndexOrThrow(column))

fun Cursor.getLong(column: String): Long =
    getLongOrNull(getColumnIndexOrThrow(column)) ?: 0L

fun Cursor.getInt(column: String): Int =
    getIntOrNull(getColumnIndexOrThrow(column)) ?: 0
