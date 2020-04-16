package com.chooloo.www.callmanager.ui.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.chooloo.www.callmanager.util.ThemeUtils;

import timber.log.Timber;

import static com.chooloo.www.callmanager.util.ThemeUtils.ThemeType;

public abstract class AbsThemeActivity extends AppCompatActivity {

    private @StyleRes int mThemeStyle = -1;
    private @ThemeType int mThemeType;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        updateTheme();
    }

    protected void setThemeType(@ThemeType int type) {
        mThemeType = type;
        updateTheme();
    }

    /**
     * Sets the theme of the app by preferences
     * <p>
     * Yes i know the way it works is a bit messy and can be cleaner but thats the quickest solution
     * i had for auto detecting system theme and i really dont have time for this
     */
    protected void updateTheme() {

        String theme = PreferenceUtils.getInstance(this).getString(R.string.pref_app_theme_key);
        String color = PreferenceUtils.getInstance(this).getString(R.string.pref_app_color_key);

        // If theme supposed to match device theme
        if (theme.equals(getString(R.string.pref_system_entry_value))) {
            theme = ThemeUtils.isNightModeOn(this) ? "dark" : "light";
        }

        Timber.i("Theme is: " + theme);
        Timber.i("Color is: " + color);

        // theme supposed to be "--theme--;--color--"
        theme = theme + ";" + color;

        int newThemeStyle = ThemeUtils.themeFromId(theme, mThemeType);
        Timber.i("Theme updating to: " + theme);
        boolean isInOnCreate = mThemeStyle == -1;

        if (mThemeStyle != newThemeStyle) {
            mThemeStyle = newThemeStyle;
            setTheme(mThemeStyle);

            if (!isInOnCreate) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    @Override
    public void setTheme(int resid) {
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(resid, true);
    }
}
