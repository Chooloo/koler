package com.chooloo.www.koler.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat

fun Context.hasNavBar(): Boolean {
    val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return id > 0 && resources.getBoolean(id)
}

fun Context.navBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
}

fun Context.getSelectableItemBackgroundDrawable(): Drawable? {
    return ContextCompat.getDrawable(this, TypedValue().also { theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true) }.resourceId)
}

fun Context.getSelectableItemBackgroundBorderlessDrawable(): Drawable? {
    return ContextCompat.getDrawable(this, TypedValue().also { theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, it, true) }.resourceId)
}

fun Context.sizeInDp(sizeInDp: Int): Int {
    val scale: Float = resources.displayMetrics.density
    return (sizeInDp * scale + 0.5f).toInt()
}