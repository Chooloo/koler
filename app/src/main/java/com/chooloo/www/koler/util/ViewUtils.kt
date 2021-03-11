package com.chooloo.www.koler.util

import android.content.Context
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
    resources.getIdentifier("navigation_bar_height", "dimen", "android").also {
        return if (it > 0) resources.getDimensionPixelSize(it) else 0
    }
}

fun Context.getSelectableItemBackgroundDrawable() = ContextCompat.getDrawable(
    this,
    TypedValue().also {
        theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true)
    }.resourceId
)

fun Context.getSelectableItemBackgroundBorderlessDrawable() = ContextCompat.getDrawable(
    this,
    TypedValue().also {
        theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, it, true)
    }.resourceId
)
