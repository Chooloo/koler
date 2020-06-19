package com.chooloo.www.callmanager.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.TypedValue;

import androidx.annotation.IntDef;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ThemeUtils {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_NORMAL, TYPE_NO_ACTION_BAR, TYPE_TRANSPARENT_STATUS_BAR})
    public @interface ThemeType {
    }

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_NO_ACTION_BAR = 1;
    public static final int TYPE_TRANSPARENT_STATUS_BAR = 2;

    public static @StyleRes
    int themeFromId(String themeId, @ThemeType int type) {
        switch (type) {
            case TYPE_NORMAL:
                return themeNormalFromId(themeId);
            case TYPE_NO_ACTION_BAR:
                return themeNoActionBarFromId(themeId);
            case TYPE_TRANSPARENT_STATUS_BAR:
                return themeTransparentStatusBarFromId(themeId);
        }
        return themeNormalFromId(themeId);
    }

    public static @StyleRes
    int themeNormalFromId(String themeId) {
        switch (themeId) {
            case "light;pink":
                return R.style.AppTheme_Light_Pink;
            case "light;cream":
                return R.style.AppTheme_Light_Cream;
            case "light;green":
                return R.style.AppTheme_Light_Green;
            case "dark;pink":
                return R.style.AppTheme_Dark_Pink;
            case "dark;green":
                return R.style.AppTheme_Dark_Green;
            case "dark;cream":
                return R.style.AppTheme_Dark_Cream;
            case "amoled;pink":
                return R.style.AppTheme_AMOLED_Pink;
            case "amoled;green":
                return R.style.AppTheme_AMOLED_Green;
            case "amoled;cream":
                return R.style.AppTheme_AMOLED_Cream;
        }
        return R.style.AppTheme_Light_Pink;
    }

    public static @StyleRes
    int themeNoActionBarFromId(String themeId) {
        switch (themeId) {
            case "light;pink":
                return R.style.AppTheme_Light_Pink_NoActionBar;
            case "light;green":
                return R.style.AppTheme_Light_Green_NoActionBar;
            case "light;cream":
                return R.style.AppTheme_Light_Cream_NoActionBar;
            case "dark;pink":
                return R.style.AppTheme_Dark_Pink_NoActionBar;
            case "dark;green":
                return R.style.AppTheme_Dark_Green_NoActionBar;
            case "dark;cream":
                return R.style.AppTheme_Dark_Cream;
            case "amoled;pink":
                return R.style.AppTheme_AMOLED_Pink_NoActionBar;
            case "amoled;green":
                return R.style.AppTheme_AMOLED_Green_NoActionBar;
            case "amoled;cream":
                return R.style.AppTheme_AMOLED_Cream;
        }
        return R.style.AppTheme_Light_Pink_NoActionBar;
    }

    public static @StyleRes
    int themeTransparentStatusBarFromId(String themeId) {
        switch (themeId) {
            case "light;pink":
                return R.style.AppTheme_Light_Pink_TransparentStatusBar;
            case "light;green":
                return R.style.AppTheme_Light_Green_TransparentStatusBar;
            case "light;cream":
                return R.style.AppTheme_Light_Cream_TransparentStatusBar;
            case "dark;pink":
                return R.style.AppTheme_Dark_Pink_TransparentStatusBar;
            case "dark;green":
                return R.style.AppTheme_Dark_Green_TransparentStatusBar;
            case "dark;cream":
                return R.style.AppTheme_Dark_Cream;
            case "amoled;pink":
                return R.style.AppTheme_AMOLED_Pink_TransparentStatusBar;
            case "amoled;green":
                return R.style.AppTheme_AMOLED_Green_TransparentStatusBar;
            case "amoled;cream":
                return R.style.AppTheme_AMOLED_Cream;
        }
        return R.style.AppTheme_Light_Pink_TransparentStatusBar;
    }

    /**
     * Check if night mode is on by system
     * @param context
     * @return is on / not
     */
    public static boolean isNightModeOn(Context context) {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                return false;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                return true;
            default:
                return false;
        }
    }

    /**
     * Return current accent color
     * @param context
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
