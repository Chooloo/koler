package com.chooloo.www.koler.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

fun Context.navBarHeight(): Int {
    val resource = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resource > 0) resources.getDimensionPixelSize(resource) else 0
}

fun Context.hasNavBar(): Boolean {
    val resource = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return resource > 0 && resources.getBoolean(resource)
}

fun Context.getSizeInDp(px: Int) =
    (px * resources.displayMetrics.density + 0.5f).toInt()

fun Context.getSelectableItemBackgroundDrawable(): Drawable? {
    val typedValue = TypedValue()
    theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
    return ContextCompat.getDrawable(this, typedValue.resourceId)
}

fun Context.getSelectableItemBackgroundBorderlessDrawable(): Drawable? {
    val typedValue = TypedValue()
    theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, typedValue, true)
    return ContextCompat.getDrawable(this, typedValue.resourceId)
}

@ColorInt
fun Context.getAttrColor(attrRes: Int): Int =
    TypedValue().also { theme.resolveAttribute(attrRes, it, true) }.data