package com.chooloo.www.koler.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat


@ColorInt
fun Context.getAttrColor(@AttrRes attrRes: Int) =
    TypedValue().also { theme.resolveAttribute(attrRes, it, true) }.data

fun Context.sizeInDp(sizeInDp: Int) = (sizeInDp * resources.displayMetrics.density + 0.5f).toInt()

fun Context.hasNavBar(): Boolean {
    resources.getIdentifier("config_showNavigationBar", "bool", "android").also {
        return it > 0 && resources.getBoolean(it)
    }
}

fun Context.navBarHeight(): Int {
    val height = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (height > 0) resources.getDimensionPixelSize(height) else 0
}

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