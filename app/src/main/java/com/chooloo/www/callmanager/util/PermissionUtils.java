package com.chooloo.www.callmanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.telecom.TelecomManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;


public class PermissionUtils {

    public static final int DEFAULT_DIALER_RC = 11;
    public static final int PERMISSION_RC = 10;
    public static final String[] MUST_HAVE_PERMISSIONS = {CALL_PHONE};
    public static final String[] OPTIONAL_PERMISSIONS = {SEND_SMS, READ_CONTACTS, READ_CALL_LOG};

    /**
     * Check if koler is set as the default dialer app
     *
     * @param activity activity
     * @return boolean
     */
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

    /**
     * Check for permissions by a given list
     * Return true *only* if all of the given permissions are granted
     *
     * @param context     from where the function is being called
     * @param permissions permission to check if granted
     * @param askForIt    whether to ask the user for the ungranted permissions
     * @return boolean is permissions granted / not
     */
    public static boolean checkPermissionsGranted(Context context, String[] permissions, boolean askForIt) {
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

    /**
     * Checks for granted permission but by a single string (single permission)
     *
     * @param context    from what context is being called
     * @param permission permission to check if granted
     * @param askForIt   whether to ask the user for the ungranted permissions
     * @return is permission granted / not
     */
    private static boolean checkPermissionGranted(Context context, String permission, boolean askForIt) {
        boolean isGranted = (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
        if (askForIt && !isGranted && context instanceof Activity)
            askForPermissions((Activity) context, new String[]{permission});
        return isGranted;
    }

    /**
     * Check is premissions granted by grant results list
     * Return true only if all permissions were granted
     *
     * @param grantResults permissions grant results
     * @return boolean is all granted / not
     */
    public static boolean checkPermissionsGranted(int[] grantResults) {
        for (int result : grantResults)
            if (result == PackageManager.PERMISSION_DENIED) return false;
        return true;
    }

    /**
     * Asks user for permissions by a given list
     *
     * @param activity    the activity that is calling the function
     * @param permissions permissions to ask the user for
     */
    public static void askForPermissions(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_RC);
    }

    /**
     * Asks user for permissions by a given list
     * From a fragment
     *
     * @param fragment    fragment from which to request the permissions
     * @param permissions permission to ask for
     */
    public static void askForPermissions(Fragment fragment, String[] permissions) {
        fragment.requestPermissions(permissions, PERMISSION_RC);
    }

}
