package com.chooloo.www.callmanager.ui.base;

import android.content.res.Resources;

import androidx.annotation.CallSuper;
import androidx.annotation.StyleRes;

import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.util.ThemeUtils;

public abstract class BaseThemeActivity extends BaseActivity {
    private @StyleRes int mThemeStyle = -1;
    private @ThemeUtils.ThemeType int mThemeType;

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

    protected void setThemeType(@ThemeUtils.ThemeType int type) {
        mThemeType = type;
        updateTheme();
    }

    /**
     * Sets the theme of the app by preferences
     * <p>
     * Yes i know the way it works is a bit messy and can be cleaner but thats the quickest solution
     * i had for auto detecting system theme and i really dont have time for this lol
     */
    protected void updateTheme() {
        setTheme(ThemeUtils.getStyleTheme(this));
        setTheme(ThemeUtils.getTypeTheme(mThemeType));
        setTheme(ThemeUtils.getAccentTheme(this));
    }

    @Override
    public void setTheme(int resid) {
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(resid, true);
    }
}
