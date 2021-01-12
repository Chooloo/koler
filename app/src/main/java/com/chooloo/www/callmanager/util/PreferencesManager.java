package com.chooloo.www.callmanager.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.StringRes;
import androidx.preference.PreferenceManager;

import com.chooloo.www.callmanager.R;

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
    private final Context mContext;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.preference, true);
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        mContext = context;
    }

    public static synchronized PreferencesManager initialize(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new PreferencesManager(context);
        }
        return sSharedPrefs;
    }

    public static synchronized PreferencesManager getInstance() {
        if (sSharedPrefs == null) {
            throw new IllegalArgumentException("Should use initialize(Context) at least once before using this method.");
        }
        return sSharedPrefs;
    }

    public static PreferencesManager getInstance(Context context) {
        PreferencesManager.initialize(context);
        return PreferencesManager.getInstance();
    }

    public void putInt(@StringRes int key, int value) {
        mPref.edit().putInt(mContext.getString(key), value).apply();
    }

    public void putString(@StringRes int key, String value) {
        mPref.edit().putString(mContext.getString(key), value).apply();
    }

    public void putBoolean(@StringRes int key, boolean value) {
        mPref.edit().putBoolean(mContext.getString(key), value).apply();
    }

    public void putFloat(@StringRes int key, float value) {
        mPref.edit().putFloat(mContext.getString(key), value).apply();
    }

    public void putLong(@StringRes int key, long value) {
        mPref.edit().putLong(mContext.getString(key), value).apply();
    }


    public int getInt(@StringRes int key, int defaultValue) {
        try {
            return mPref.getInt(mContext.getString(key), defaultValue);
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public String getString(@StringRes int key, String defaultValue) {
        try {
            return mPref.getString(mContext.getString(key), defaultValue);
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(@StringRes int key, boolean defaultValue) {
        try {
            return mPref.getBoolean(mContext.getString(key), defaultValue);
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public float getFloat(@StringRes int key, float defaultValue) {
        try {
            return mPref.getFloat(mContext.getString(key), defaultValue);
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public long getLong(@StringRes int key, long defaultValue) {
        try {
            return mPref.getLong(mContext.getString(key), defaultValue);
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }
}