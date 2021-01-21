package com.chooloo.www.callmanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telecom.TelecomManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class PermissionUtils {

    public static final int DEFAULT_DIALER_RC = 11;
    public static final int PERMISSION_RC = 10;

    public static boolean checkDefaultDialer(Activity activity) {
        String packageName = activity.getApplication().getPackageName();
        try {
            if (!activity.getSystemService(TelecomManager.class).getDefaultDialerPackage().equals(packageName)) {
                // Prompt the user with a dialog to select this app to be the default phone app
                Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                        .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName);
                activity.startActivityForResult(intent, DEFAULT_DIALER_RC);
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkPermissions(Context context, String[] permissions, boolean askForIt) {
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }
        if (deniedPermissions.size() > 0 && askForIt && context instanceof Activity) {
            askForPermissions((Activity) context, deniedPermissions.toArray(new String[deniedPermissions.size()]));
        }
        return deniedPermissions.size() <= 0;
    }

    public static boolean checkPermission(Context context, String permission, boolean askForIt) {
        boolean isGranted = (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
        if (askForIt && !isGranted && context instanceof Activity)
            askForPermissions((Activity) context, new String[]{permission});
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

    public static void askForPermissions(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_RC);
    }

    public static void askForPermissions(Fragment fragment, String[] permissions) {
        fragment.requestPermissions(permissions, PERMISSION_RC);
    }

}
