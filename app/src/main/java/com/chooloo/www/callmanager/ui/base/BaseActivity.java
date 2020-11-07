package com.chooloo.www.callmanager.ui.base;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.chooloo.www.callmanager.util.Utilities;

import butterknife.ButterKnife;

import static com.chooloo.www.callmanager.util.PermissionUtils.PERMISSION_RC;

public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    private static final int PERMISSION_RC = 10;
    protected String[] mRequiredPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.setUpLocale(this);
        PreferenceUtils.getInstance(this);
        ButterKnife.bind(this);
        setUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void setRequiredPermissions(String[] permissions) {
        this.mRequiredPermissions = permissions;
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

    protected abstract void setUp();
}
