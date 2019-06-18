package com.chooloo.www.callmanager.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.SparseArray;

import androidx.annotation.StringRes;

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
public class PreferenceUtils {

    @SuppressLint("StaticFieldLeak")
    private static PreferenceUtils sSharedPrefs;

    private Context mContext;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private boolean mBulkUpdate = false;

    private static final SparseArray<Object> sDefaultValues = new SparseArray<>();

    /**
     * Constructor
     *
     * @param context
     */
    private PreferenceUtils(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        mContext = context;

        sDefaultValues.put(R.string.pref_app_theme_key, context.getString(R.string.pref_app_theme_default_value));
        sDefaultValues.put(R.string.pref_reject_call_timer_key, context.getString(R.string.pref_reject_call_timer_default_value));
        sDefaultValues.put(R.string.pref_answer_call_timer_key, context.getString(R.string.pref_answer_call_timer_default_value));
        sDefaultValues.put(R.string.pref_default_page_key, context.getString(R.string.pref_default_page_default_value));
        sDefaultValues.put(R.string.pref_is_first_instance_key, context.getResources().getBoolean(R.bool.pref_is_first_instance_default_value));
        sDefaultValues.put(R.string.pref_last_version_key, -1);
    }

    public static PreferenceUtils getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new PreferenceUtils(context.getApplicationContext());
        }
        return sSharedPrefs;
    }

    public static PreferenceUtils getInstance() {
        if (sSharedPrefs != null) {
            return sSharedPrefs;
        }

        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");
    }

    public Object getDefaultValue(@StringRes int key) {
        return sDefaultValues.get(key);
    }

    public void putInt(@StringRes int key, int value) {
        doEdit();
        mEditor.putInt(mContext.getString(key), value);
        doCommit();
    }

    public void putString(@StringRes int key, String value) {
        doEdit();
        mEditor.putString(mContext.getString(key), value);
        doCommit();
    }

    public void putBoolean(@StringRes int key, boolean value) {
        doEdit();
        mEditor.putBoolean(mContext.getString(key), value);
        doCommit();
    }

    public void putFloat(@StringRes int key, float value) {
        doEdit();
        mEditor.putFloat(mContext.getString(key), value);
        doCommit();
    }

    public void putLong(@StringRes int key, long value) {
        doEdit();
        mEditor.putLong(mContext.getString(key), value);
        doCommit();
    }

    public int getInt(@StringRes int key) {
        return mPref.getInt(mContext.getString(key), (int) getDefaultValue(key));
    }

    public String getString(@StringRes int key) {
        return mPref.getString(mContext.getString(key), (String) getDefaultValue(key));
    }

    public boolean getBoolean(@StringRes int key) {
        return mPref.getBoolean(mContext.getString(key), (boolean) getDefaultValue(key));
    }

    public float getFloat(@StringRes int key) {
        return mPref.getFloat(mContext.getString(key), (float) getDefaultValue(key));
    }

    public long getLong(@StringRes int key) {
        return mPref.getLong(mContext.getString(key), (long) getDefaultValue(key));
    }

    @SuppressLint("CommitPrefEdits")
    public void edit() {
        mBulkUpdate = true;
        mEditor = mPref.edit();
    }

    public void commit() {
        mBulkUpdate = false;
        mEditor.commit();
        mEditor = null;
    }

    @SuppressLint("CommitPrefEdits")
    private void doEdit() {
        if (!mBulkUpdate && mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }
}