package com.chooloo.www.koler.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import com.chooloo.www.koler.R

class ContactAvatarHelper(private val context: Context) {
    @SuppressLint("NewApi")
    fun getCallContactAvatar(photoUri: String?): Bitmap? {
        var bitmap: Bitmap? = null
        if (photoUri?.isNotEmpty() == true) {
            val uri = Uri.parse(photoUri)
            try {
                val contentResolver = context.contentResolver
                bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val tmbSize = context.resources.getDimension(R.dimen.list_avatar_size).toInt()
                    contentResolver.loadThumbnail(uri, Size(tmbSize, tmbSize), null)
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }
                bitmap = getCircularBitmap(bitmap!!)
            } catch (ignored: Exception) {
                return null
            }
        }
        return bitmap
    }

    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.width, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val radius = bitmap.width / 2.toFloat()

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(radius, radius, radius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }
}
