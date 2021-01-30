package com.chooloo.www.callmanager.util

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import androidx.annotation.IntDef
import com.chooloo.www.callmanager.R

object ThemeUtils {
    const val TYPE_NORMAL = R.style.AppTheme
    const val TYPE_NO_ACTION_BAR = R.style.AppTheme_NoActionBar

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(TYPE_NORMAL, TYPE_NO_ACTION_BAR)
    annotation class ThemeType

    /**
     * Check if night mode is on by system
     *
     * @param context context
     * @return is on / not
     */
    private fun isNightModeOn(context: Context): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * Return current accent color
     *
     * @param context context
     * @return color int value
     */
    @JvmStatic
    fun getAccentColor(context: Context): Int {
        val a = context.obtainStyledAttributes(TypedValue().data, intArrayOf(R.attr.colorAccent))
        val color = a.getColor(0, 0)
        a.recycle()
        return color
    }
}