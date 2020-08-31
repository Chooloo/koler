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

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_NORMAL, TYPE_NO_ACTION_BAR, TYPE_TRANSPARENT_STATUS_BAR})
    public @interface ThemeType {
    }

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_NO_ACTION_BAR = 1;
    public static final int TYPE_TRANSPARENT_STATUS_BAR = 2;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({STYLE_LIGHT, STYLE_DARK, STYLE_AMOLED, STYLE_SYSTEM})
    private @interface ThemeStyle {
    }

    private static final String STYLE_LIGHT = "light";
    private static final String STYLE_DARK = "dark";
    private static final String STYLE_AMOLED = "amoled";
    private static final String STYLE_SYSTEM = "system";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ACCENT_BLUE, ACCENT_PINK, ACCENT_GREEN, ACCENT_CREAM})
    private @interface AccentColor {
    }

    private static final String ACCENT_BLUE = "blue";
    private static final String ACCENT_PINK = "pink";
    private static final String ACCENT_GREEN = "green";
    private static final String ACCENT_CREAM = "cream";

    private static final int DEFAULT_ACCENT = R.style.Accent_Blue;

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
     * Return the theme that corresponds to theme style the user has chosen
     *
     * @param context context
     * @return style theme resource
     */
    public static @StyleRes
    int getStyleTheme(Context context) {
        // get the user's choice
        @ThemeStyle String style = PreferenceUtils.getInstance(context).getString(R.string.pref_app_theme_key);

        // if the user chose "system"
        if (style.equals(STYLE_SYSTEM))
            style = ThemeUtils.isNightModeOn(context) ? STYLE_DARK : STYLE_LIGHT;

        switch (style) {
            case STYLE_LIGHT:
                return R.style.AppTheme_Light;
            case STYLE_DARK:
                return R.style.AppTheme_Dark;
            case STYLE_AMOLED:
                return R.style.AppTheme_AMOLED;
            default:
                return R.style.AppTheme_Light;
        }
    }

    /**
     * Return the theme that corresponds the the user's choice for the accent color
     *
     * @param context Context
     * @return accent color theme resource
     */
    public static @StyleRes
    int getAccentTheme(Context context) {
        @AccentColor String color = PreferenceUtils.getInstance(context).getString(R.string.pref_app_color_key);
        if (color == null) return DEFAULT_ACCENT;
        switch (color) {
            case ACCENT_BLUE:
                return R.style.Accent_Blue;
            case ACCENT_PINK:
                return R.style.Accent_Pink;
            case ACCENT_GREEN:
                return R.style.Accent_Green;
            case ACCENT_CREAM:
                return R.style.Accent_Cream;
            default:
                return DEFAULT_ACCENT;
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
