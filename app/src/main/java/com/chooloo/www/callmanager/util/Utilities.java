package com.chooloo.www.callmanager.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.chooloo.www.callmanager.activity.MainActivity;

import org.jetbrains.annotations.NotNull;

import androidx.core.content.ContextCompat;
import timber.log.Timber;

import static android.Manifest.permission.READ_CONTACTS;

public class Utilities {

    public final static long DEFAULT_VIBRATE_LENGTH = 100;

    /**
     * Checks for a given permission and returns true if true and false if false
     *
     * @param context
     * @param permission a string of the permission (Manifest.permission.***)
     * @return boolean true/false
     */
    public static boolean checkStrPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    /**
     * Vibrate the phone for {@code DEFAULT_VIBRATE_LENGTH} milliseconds
     */
    public static void vibrate(@NotNull Context context) {
        vibrate(context, DEFAULT_VIBRATE_LENGTH);
    }

    /**
     * Vibrate the phone
     *
     * @param millis the amount of milliseconds to vibrate the phone for.
     */
    public static void vibrate(@NotNull Context context, long millis) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(millis);
        }
    }

    /**
     * Get the dpi for this phone
     *
     * @return the dpi
     */
    public static float dpi(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.densityDpi;
    }

    /**
     * Get whether a given x and y coordinates are in the vicinity of a view
     *
     * @param view                 the target view
     * @param x                    the x value of the point - must be raw
     * @param y                    the y value of the point - must be raw
     * @param buttonVicinityOffset the vicinity in which to check the condition - in dp
     * @return true if the point is in the view's vicinity, false otherwise.
     */
    public static boolean inViewInBounds(View view, int x, int y, int buttonVicinityOffset) {
        Rect outRect = new Rect();
        int[] location = new int[2];
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);

        outRect.offset(location[0], location[1]);

        int e = (int) (buttonVicinityOffset * (dpi(view.getContext()) / DisplayMetrics.DENSITY_DEFAULT));
        outRect = new Rect(outRect.left - e,
                outRect.top - e,
                outRect.right + e,
                outRect.bottom + e);

        Timber.d("outRect: %s, x and y: %d, %d", outRect.toShortString(), x, y);

        return outRect.contains(x, y);
    }

    /**
     * Toggle the active state of a view
     * @param view the view to toggle
     */
    public static void toggleViewActivation(View view) {
        view.setActivated(!view.isActivated());

    }

    /**
     * Format a given phone number to a readable string for the user
     * @param phoneNumber the number to format
     * @return the formatted number
     */
    public static String formatPhoneNumber(String phoneNumber) {
        String lastPhoneNumber = phoneNumber;
        if (phoneNumber != null) { // Checks for unusual occurrences of a number
            if (lastPhoneNumber.contains("+972"))
                lastPhoneNumber = lastPhoneNumber.replace("+972", "0");
            if (lastPhoneNumber.contains(" ")) lastPhoneNumber = lastPhoneNumber.replace(" ", "");
            if (lastPhoneNumber.contains("-")) lastPhoneNumber = lastPhoneNumber.replace("-", "");
        }
        return lastPhoneNumber;
    }
}