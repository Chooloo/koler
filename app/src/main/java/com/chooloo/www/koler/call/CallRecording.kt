package com.chooloo.www.koler.call

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Audio.Media
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi

data class CallRecording(
    val number: String,
    val filename: String,
    val creationTime: Long,
    val mediaId: Long? = null
) {

    companion object {
        const val RELATIVE_PATH = "Music/Koler Call Recordings"

        @RequiresApi(Build.VERSION_CODES.Q)
        fun generateMediaInsertValues(filename: String, creationTime: Long): ContentValues {
            val cv = ContentValues(5)
            val extension = MimeTypeMap.getFileExtensionFromUrl(filename)
            val mime = if (extension.isEmpty()) "audio/*" else MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(extension)

            return cv.apply {
                put(Media.RELATIVE_PATH, RELATIVE_PATH)
                put(Media.DISPLAY_NAME, filename)
                put(Media.DATE_TAKEN, creationTime)
                put(Media.MIME_TYPE, mime)
                put(Media.IS_PENDING, 1)
            }
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        fun generateCompletedValues() = ContentValues(1).apply {
            put(Media.IS_PENDING, 0)
        }
    }
}