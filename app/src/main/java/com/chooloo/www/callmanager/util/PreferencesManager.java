package com.chooloo.www.callmanager.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;

import androidx.annotation.StringRes;
import androidx.preference.PreferenceManager;

/*
 * A Singleton for managing your SharedPreferences.
 *_
 * IMPORTANT: The class is not thread safe. It should work fine in most
 * circumstances since the write and read operations are fast. However
 * if you call edit for bulk updates and do not commit your changes
 * there is a possibility of data loss if a background thread has modified
 * preferences at the same time.
 */
public class PreferencesManager {

    @SuppressLint("StaticFieldLeak")
    private static PreferencesManager sSharedPrefs;
    private SharedPreferences mPref;

    /**
     * Constructor
     *
     * @param context
     */
    private PreferencesManager(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized PreferencesManager initialize(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new PreferencesManager(context);
        }
        return sSharedPrefs;
    }

    public static synchronized PreferencesManager getInstance() {
        if (sSharedPrefs == null) {
            throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");
        }
        return sSharedPrefs;
    }

    public Object getDefaultValue(@StringRes int key) {
        return sDefaultValues.get(key);
    }


    public void putInt(String key, int value) {
        mPref.edit().putInt(key, value).commit();
    }

    public void putString(String key, String value) {
        mPref.edit().putString(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        mPref.edit().putBoolean(String key, value).commit();
    }

    public void putFloat(String key, float value) {
        mPref.edit().putFloat(key, value).commit();
    }

    public void putLong(String key, long value) {
        mPref.edit().putLong(String key, value).commit();
    }


    public int getInt(String key, int defaultValue) {
        return mPref.getInt(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return mPref.getFloat(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return mPref.getLong(key, defaultValue);
    }
}