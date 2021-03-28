package com.chooloo.www.koler.util.callrecord

import android.content.ContentValues
import android.provider.MediaStore.Audio.Media
import android.webkit.MimeTypeMap

data class CallRecording(
    val number: String,
    val filename: String,
    val creationTime: Long,
    val mediaId: Long?=null
) {
    companion object {
        const val RELATIVE_PATH = "Music/Koler Call Recordings"

        fun generateMediaInsertValues(filename: String, creationTime: Long): ContentValues {
            val contentValues = ContentValues(5)
            contentValues.apply {
                put(Media.RELATIVE_PATH, RELATIVE_PATH)
                put(Media.DISPLAY_NAME, filename)
                put(Media.DATE_TAKEN, creationTime)
                put(Media.IS_PENDING, 1)
            }
            val extension = MimeTypeMap.getFileExtensionFromUrl(filename)
            val mime = if (extension.isEmpty()) "audio/*" else MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(extension)
            contentValues.put(Media.MIME_TYPE, mime)
            return contentValues
        }

        fun generateCompletedValues() = ContentValues(1).apply {
            put(Media.IS_PENDING, 0)
        }
    }
}