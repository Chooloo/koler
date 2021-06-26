package com.chooloo.www.koler.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

class ViewManager(private val _context: Context) {
    @ColorInt
    fun getAttrColor(@AttrRes attrRes: Int) =
        TypedValue().also { _context.theme.resolveAttribute(attrRes, it, true) }.data

    fun sizeInDp(sizeInDp: Int) =
        (sizeInDp * _context.resources.displayMetrics.density + 0.5f).toInt()

    fun hasNavBar(): Boolean {
        _context.resources.getIdentifier("config_showNavigationBar", "bool", "android").also {
            return it > 0 && _context.resources.getBoolean(it)
        }
    }

    fun navBarHeight(): Int {
        val height = _context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (height > 0) _context.resources.getDimensionPixelSize(height) else 0
    }

    fun getSelectableItemBackgroundDrawable(): Drawable? {
        val typedValue = TypedValue()
        _context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
        return ContextCompat.getDrawable(_context, typedValue.resourceId)
    }

    fun getSelectableItemBackgroundBorderlessDrawable(): Drawable? {
        val typedValue = TypedValue()
        _context.theme.resolveAttribute(
            android.R.attr.selectableItemBackgroundBorderless,
            typedValue,
            true
        )
        return ContextCompat.getDrawable(_context, typedValue.resourceId)
    }
}