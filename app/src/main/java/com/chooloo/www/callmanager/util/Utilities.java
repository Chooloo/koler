package com.chooloo.www.callmanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static android.Manifest.permission.SEND_SMS;

public class Utilities {

    public static final long LONG_VIBRATE_LENGTH = 500;
    public static final long SHORT_VIBRATE_LENGTH = 20;
    public static final long DEFAULT_VIBRATE_LENGTH = 100;
    public static Locale sLocale;

    public static void setUpLocale(@NonNull Context context) {
        Utilities.sLocale = context.getResources().getSystem().getConfiguration().getLocales().get(0);
    }

    public static String getCountry(@NonNull Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimCountryIso().toUpperCase();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        else vibrator.vibrate(millis);
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
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param context Context to get resources and device specific display metrics
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(Context context, float dp) {
        return dp * (dpi(context) / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param context Context to get resources and device specific display metrics
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(Context context, float px) {
        return px / (dpi(context) / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * Returns whither currently in UI thread
     *
     * @return boolean
     */
    public static boolean isInUIThread() {
        return Looper.getMainLooper().isCurrentThread();
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

        int e = (int) (convertDpToPixel(view.getContext(), buttonVicinityOffset));
        outRect = new Rect(outRect.left - e,
                outRect.top - e,
                outRect.right + e,
                outRect.bottom + e);

        Timber.d("outRect: %s, x and y: %d, %d", outRect.toShortString(), x, y);

        return outRect.contains(x, y);
    }

    /**
     * Toggle the active state of a view
     *
     * @param view the view to toggle
     */
    public static void toggleViewActivation(View view) {
        view.setActivated(!view.isActivated());
    }

    /**
     * Builds a string without the separators
     *
     * @param list
     * @param separator
     * @return String
     */
    public static String joinStringsWithSeparator
    (@NotNull List<String> list, @NotNull String separator) {
        if (list.size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            builder.append(str);
            builder.append(separator);
        }
        String result = builder.toString();
        return result.substring(0, result.length() - separator.length());
    }

    /**
     * Send sms by given phone number and message
     *
     * @param phoneNum destination phone number (where to send the sms to)
     * @param msg      the content message of the sms
     */
    public static void sendSMS(FragmentActivity activity, String phoneNum, String msg) {
        if (ContextCompat.checkSelfPermission(activity, SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager.getDefault().sendTextMessage(CallManager.getDisplayContact(activity).getMainPhoneNumber(), null, msg, null, null);
                Toast.makeText(activity, "Message Sent", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, "Oh shit I can't send the message... Sorry", Toast.LENGTH_LONG).show();
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{SEND_SMS}, 1);
        }
    }

    /**
     * Returns whither the device has an active navigation bar
     *
     * @param context
     * @return boolean
     */
    public static boolean hasNavBar(Context context) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    /**
     * Returns the navigation bar height
     *
     * @param context
     * @return int
     */
    public static int navBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) return resources.getDimensionPixelSize(resourceId);
        return 0;
    }

    /**
     * Toggles the keyboard according to given parameter (boolean)
     *
     * @param context
     * @param view
     * @param isToOpen
     */
    public static void toggleKeyboard(Context context, View view, boolean isToOpen) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isToOpen) imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        else
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    /**
     * Returns only the numbers from a string (removes special characters and spaces
     *
     * @param string the string to get all the numbers from
     * @return string the numbers extracted from the given string
     */
    public static String getOnlyNumbers(String string) {
        return string.replaceAll("[^0-9#*]", "");
    }

    /**
     * Opens a new sms with a given number filled in
     *
     * @param activity
     * @param number
     */
    public static void openSmsWithNumber(Activity activity, String number) {
        if (activity == null) return;
        Uri uri = Uri.parse(String.format("smsto:%s", number));
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
        activity.startActivity(smsIntent);
    }

}