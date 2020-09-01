package com.chooloo.www.callmanager.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.chooloo.www.callmanager.util.ThemeUtils;

import org.openxmlformats.schemas.drawingml.x2006.main.impl.ThemeDocumentImpl;

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
