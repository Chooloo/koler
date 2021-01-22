package com.chooloo.www.callmanager.ui.base;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.chooloo.www.callmanager.util.PreferencesManager;
import com.chooloo.www.callmanager.util.Utilities;

import static com.chooloo.www.callmanager.util.PermissionUtils.RC_DEFAULT;

public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    protected String[] mRequiredPermissions;

    protected PreferencesManager mPreferences;

    protected ViewModelProvider mViewModelProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.setUpLocale(this);
        mPreferences = PreferencesManager.getInstance(this);
        mRequiredPermissions = onGetPermissions();
        mViewModelProvider = new ViewModelProvider(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onSetup();
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

    @Override
    public void askForPermission(String permission, int requestCode) {
        requestPermissions(new String[]{permission}, requestCode);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void askForPermissions(String[] permissions, int requestCode) {
        requestPermissions(permissions, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void askForPermissions() {
        requestPermissions(mRequiredPermissions, RC_DEFAULT);
    }

    @Override
    public void showMessage(String message) {
        // TODO implement a custom scack bar
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(int stringResId) {
        showMessage(getString(stringResId));
    }

    @Override
    public void showError(String message) {
        // TODO implement a custom scack bar
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(int stringResId) {
        showError(getString(stringResId));
    }
}
