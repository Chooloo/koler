package com.chooloo.www.callmanager.ui.base;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.chooloo.www.callmanager.util.PreferencesManager;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    private static final int PERMISSION_RC = 10;
    protected String[] mRequiredPermissions;
    protected PreferencesManager mPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.setUpLocale(this);
        mPreferences = PreferencesManager.getInstance(this);
        mRequiredPermissions = onGetPermissions();
    }

    @Override
    public String[] onGetPermissions() {
        return new String[]{};
    }

    @Override
    public boolean hasPermission(String permission) {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean hasPermissions() {
        for (String permission : mRequiredPermissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void askForPermissions(String[] permissions, int requestCode) {
        requestPermissions(permissions, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void askForPermissions() {
        requestPermissions(mRequiredPermissions, PERMISSION_RC);
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "And error occured", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(int stringResId) {
        showMessage(getString(stringResId));
    }

    @Override
    public void showError(String message) {
        // TODO show a snack bar or something
    }

    @Override
    public void showError(int stringResId) {
        showError(getString(stringResId));
    }
}
