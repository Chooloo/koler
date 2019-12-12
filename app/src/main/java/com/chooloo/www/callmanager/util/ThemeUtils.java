package com.chooloo.www.callmanager.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

import androidx.annotation.IntDef;
import androidx.annotation.StyleRes;

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
            case "light;green":
                return R.style.AppTheme_Light_Green;
            case "dark;pink":
                return R.style.AppTheme_Dark_Pink;
            case "dark;green":
                return R.style.AppTheme_Dark_Green;
            case "amoled;pink":
                return R.style.AppTheme_AMOLED_Pink;
            case "amoled;green":
                return R.style.AppTheme_AMOLED_Green;
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
            case "dark;pink":
                return R.style.AppTheme_Dark_Pink_NoActionBar;
            case "dark;green":
                return R.style.AppTheme_Dark_Green_NoActionBar;
            case "amoled;pink":
                return R.style.AppTheme_AMOLED_Pink_NoActionBar;
            case "amoled;green":
                return R.style.AppTheme_AMOLED_Green_NoActionBar;
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
            case "dark;pink":
                return R.style.AppTheme_Dark_Pink_TransparentStatusBar;
            case "dark;green":
                return R.style.AppTheme_Dark_Green_TransparentStatusBar;
            case "amoled;pink":
                return R.style.AppTheme_AMOLED_Pink_TransparentStatusBar;
            case "amoled;green":
                return R.style.AppTheme_AMOLED_Green_TransparentStatusBar;
        }
        return R.style.AppTheme_Light_Pink_TransparentStatusBar;
    }

    public static int getAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.secondaryAccentColor});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
}
