package com.chooloo.www.koler.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

class ViewManager(private val context: Context) {
    val navBarHeight: Int
        get() {
            val resource =
                context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (resource > 0) context.resources.getDimensionPixelSize(resource) else 0
        }

    val hasNavBar: Boolean
        get() {
            val resource =
                context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
            return resource > 0 && context.resources.getBoolean(resource)
        }

    val selectableItemBackgroundDrawable: Drawable?
        get() {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                android.R.attr.selectableItemBackground,
                typedValue,
                true
            )
            return ContextCompat.getDrawable(context, typedValue.resourceId)
        }

    val selectableItemBackgroundBorderlessDrawable: Drawable?
        get() {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(
                android.R.attr.selectableItemBackgroundBorderless,
                typedValue,
                true
            )
            return ContextCompat.getDrawable(context, typedValue.resourceId)
        }


    fun getSizeInDp(sizeInDp: Int): Int =
        (sizeInDp * context.resources.displayMetrics.density + 0.5f).toInt()

    @ColorInt
    fun getAttrColor(attrRes: Int): Int =
        TypedValue().also { context.theme.resolveAttribute(attrRes, it, true) }.data
}