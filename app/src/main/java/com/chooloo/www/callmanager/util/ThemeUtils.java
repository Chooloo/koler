package com.chooloo.www.callmanager.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.TypedValue;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;
import androidx.annotation.StyleRes;

import com.chooloo.www.callmanager.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ThemeUtils {

    public static final int TYPE_NORMAL = R.style.ThemeType;
    public static final int TYPE_NO_ACTION_BAR = R.style.ThemeType_NoActionBar;
    public static final int TYPE_TRANSPARENT_STATUS_BAR = R.style.ThemeType_TransparentStatusBar;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_NORMAL, TYPE_NO_ACTION_BAR, TYPE_TRANSPARENT_STATUS_BAR})
    public @interface ThemeType {
    }

    /**
     * Return the theme that corresponds to the correct theme type
     *
     * @param type int
     * @return type theme resource
     */
    public static @StyleRes
    int getTypeTheme(@ThemeType int type) {
        switch (type) {
            case TYPE_NORMAL:
                return R.style.ThemeType;
            case TYPE_NO_ACTION_BAR:
                return R.style.ThemeType_NoActionBar;
            case TYPE_TRANSPARENT_STATUS_BAR:
                return R.style.ThemeType_TransparentStatusBar;
            default:
                return R.style.ThemeType;
        }
    }

    /**
     * Check if night mode is on by system
     *
     * @param context context
     * @return is on / not
     */
    private static boolean isNightModeOn(Context context) {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    /**
     * Return current accent color
     *
     * @param context context
     * @return color int value
     */
    public static int getAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.secondaryAccentColor});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }
}
