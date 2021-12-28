package com.chooloo.www.koler.util

import android.content.Context
import androidx.annotation.BoolRes
import androidx.annotation.StringRes
import androidx.preference.PreferenceManager
import com.chooloo.www.koler.R

/*
 * A Singleton for managing your SharedPreferences.
 *_w
 * IMPORTANT: The class is not thread safe. It should work fine in most
 * circumstances since the write and read operations are fast. However
 * if you call edit for bulk updates and do not commit your changes
 * there is a possibility of data loss if a background thread has modified
 * preferences at the same time.
 */
class PreferencesManager private constructor(private val _context: Context) {
    private val _pref by lazy { PreferenceManager.getDefaultSharedPreferences(_context) }


    init {
        PreferenceManager.setDefaultValues(_context, R.xml.preferences_main, false)
    }


    fun putInt(@StringRes key: Int, value: Int) {
        _pref.edit().putInt(_context.getString(key), value).apply()
    }

    fun putLong(@StringRes key: Int, value: Long) {
        _pref.edit().putLong(_context.getString(key), value).apply()
    }

    fun putFloat(@StringRes key: Int, value: Float) {
        _pref.edit().putFloat(_context.getString(key), value).apply()
    }

    fun putString(@StringRes key: Int, value: String?) {
        _pref.edit().putString(_context.getString(key), value).apply()
    }

    fun putBoolean(@StringRes key: Int, value: Boolean) {
        _pref.edit().putBoolean(_context.getString(key), value).apply()
    }


    fun getInt(@StringRes key: Int, defaultValue: Int) =
        _pref.getInt(_context.getString(key), defaultValue)

    fun getLong(@StringRes key: Int, defaultValue: Long) =
        _pref.getLong(_context.getString(key), defaultValue)

    fun getFloat(@StringRes key: Int, defaultValue: Float) =
        _pref.getFloat(_context.getString(key), defaultValue)

    fun getString(@StringRes key: Int, defaultValue: String? = null) =
        _pref.getString(_context.getString(key), defaultValue)

    fun getBoolean(@StringRes keyRes: Int, @BoolRes defaultValueRes: Int) =
        _pref.getBoolean(_context.getString(keyRes), _context.resources.getBoolean(defaultValueRes))


    companion object : SingletonHolder<PreferencesManager, Context>(::PreferencesManager)
}