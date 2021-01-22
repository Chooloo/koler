package com.chooloo.www.callmanager.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telecom.TelecomManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.ui.base.BaseActivity;


public class PermissionUtils {

    public static final int RC_DEFAULT = 0;
    public static final int RC_WAKE_LOCK = 1;
    public static final int RC_MANAGE_OWN_CALLS = 2;
    public static final int RC_CALL_PHONE = 3;
    public static final int RC_READ_PHONE_STATE = 4;
    public static final int RC_READ_CONTACTS = 5;
    public static final int RC_READ_CALL_LOG = 6;
    public static final int RC_WRITE_CALL_LOG = 7;
    public static final int RC_SEND_SMS = 8;
    public static final int RC_WRITE_CONTACTS = 9;
    public static final int RC_VIBRATE = 10;
    public static final int RC_MODIFY_AUDIO_SETTINGS = 11;
    public static final int RC_READ_EXTERNAL_STORAGE = 12;
    public static final int RC_WRITE_EXTERNAL_STORAGE = 13;
    public static final int RC_RECORD_AUDIO = 14;
    public static final int RC_USE_BIOMETRIC = 15;
    public static final int RC_DEFAULT_DIALER = 16;

    public static boolean isDefaultDialer(BaseActivity activity) {
        String kolerPackageName = activity.getApplication().getPackageName();
        String dialerPackageName = activity.getSystemService(TelecomManager.class).getDefaultDialerPackage();
        return dialerPackageName.equals(kolerPackageName);
    }

    public static void ensureDefaultDialer(BaseActivity activity) {
        if (!isDefaultDialer(activity)) {
            Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, activity.getPackageName());
            activity.startActivityForResult(intent, RC_DEFAULT_DIALER);
        }
    }

    public static boolean checkPermission(Context context, String permission, boolean askForIt) {
        boolean isGranted = (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
        if (askForIt && !isGranted && context instanceof BaseActivity) {
            ActivityCompat.requestPermissions((BaseActivity) context, new String[]{permission}, RC_DEFAULT);
        }
        return isGranted;
    }

    public static boolean checkPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
