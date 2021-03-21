package com.chooloo.www.koler.util.preferences

import android.content.Context
import androidx.annotation.StyleRes
import com.chooloo.www.koler.R

class PreferencesWrapper(
    private val context: Context
) {
    private val _pref by lazy { PreferencesManager(context) }

    val theme: Int
        @StyleRes
        get() = when (_pref.getString(
            R.string.pref_key_color,
            context.getString(R.string.pref_color_value_blue)
        )) {
            context.getString(R.string.pref_color_value_red) -> R.style.Accent_Red
            context.getString(R.string.pref_color_value_blue) -> R.style.Accent_Blue
            context.getString(R.string.pref_color_value_green) -> R.style.Accent_Green
            context.getString(R.string.pref_color_value_orange) -> R.style.Accent_Orange
            context.getString(R.string.pref_color_value_purple) -> R.style.Accent_Purple
            else -> R.style.Accent_Blue
        }
}