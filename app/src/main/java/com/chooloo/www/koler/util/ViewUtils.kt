package com.chooloo.www.koler.util

import android.content.Context
import android.util.TypedValue

fun Context.hasNavBar(): Boolean {
    val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return id > 0 && resources.getBoolean(id)
}

fun Context.navBarHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
}

fun Context.getSelectableItemBackgroundDrawable(): Int {
    return (TypedValue().also {
        theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true);
    }).resourceId
}