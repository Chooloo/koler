package com.chooloo.www.callmanager.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;

public class Utilities {

    public static final int DEFAULT_DIALER_RC = 11;
    public static final int PERMISSION_RC = 10;
    public static final String[] MUST_HAVE_PERMISSIONS = {CALL_PHONE, READ_CONTACTS, READ_CALL_LOG};
    public static final String[] OPTIONAL_PERMISSIONS = {SEND_SMS};

    public static Locale sLocale;

    // Constants
    public static final long LONG_VIBRATE_LENGTH = 500;
    public static final long SHORT_VIBRATE_LENGTH = 20;
    public static final long DEFAULT_VIBRATE_LENGTH = 100;

    public static void setUpLocale(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Utilities.sLocale = context.getResources().getSystem().getConfiguration().getLocales().get(0);
        } else {
            Utilities.sLocale = context.getResources().getSystem().getConfiguration().locale;
        }
    }

    public static boolean checkDefaultDialer(FragmentActivity activity) {
        // Prompt the user with a dialog to select this app to be the default phone app
        String packageName = activity.getApplication().getPackageName();
        if (!activity.getSystemService(TelecomManager.class).getDefaultDialerPackage().equals(packageName)) {
            Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName);
            activity.startActivityForResult(intent, DEFAULT_DIALER_RC);
            return false;
        }
        return true;
    }

    /**
     * Checks for granted permission but by a single string (single permission)
     *
     * @param permission
     * @return boolean
     */
    public static boolean checkPermissionsGranted(Context context, String permission) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(
                context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Check for permissions by a given list
     *
     * @param permissions
     * @return boolean
     */
    public static boolean checkPermissionsGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (!checkPermissionsGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check for permissions by a given list
     *
     * @param grantResults
     * @return boolean
     */
    public static boolean checkPermissionsGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    public static void askForPermission(FragmentActivity activity, String permission) {
        askForPermissions(activity, new String[]{permission});
    }

    /**
     * Asks for permissions by a given list
     *
     * @param activity
     * @param permissions
     */
    public static void askForPermissions(FragmentActivity activity, String[] permissions) {
        activity.requestPermissions(permissions, PERMISSION_RC);
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
     * Format a given phone number to a readable string for the user
     *
     * @param phoneNumber the number to format
     * @return the formatted number
     */
    public static String formatPhoneNumber(String phoneNumber) {

        if (phoneNumber == null) return null;
        phoneNumber = normalizePhoneNumber(phoneNumber);

        Phonenumber.PhoneNumber formattedNumber = null;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        try {
            formattedNumber = phoneUtil.parse(phoneNumber, sLocale.getCountry());
        } catch (NumberParseException e) {
            Timber.w(e);
        }

        // return the number
        if (formattedNumber == null) return phoneNumber;
        else {
            PhoneNumberUtil.PhoneNumberFormat format;
            if (phoneUtil.getRegionCodeForCountryCode(formattedNumber.getCountryCode()).equals(sLocale.getCountry()))
                format = PhoneNumberUtil.PhoneNumberFormat.NATIONAL;
            else
                format = PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;

            return phoneUtil.format(formattedNumber, format);
        }
    }

    public static String normalizePhoneNumber(String phoneNumber) {
        return PhoneNumberUtil.normalizeDiallableCharsOnly(phoneNumber);
    }

    /**
     * Builds a string without the separators
     *
     * @param list
     * @param separator
     * @return String
     */
    public static String joinStringsWithSeparator(@NotNull List<String> list, @NotNull String separator) {
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
                SmsManager smsManager = SmsManager.getDefault();
                Timber.i("Sending sms to phone number: %s", CallManager.getDisplayContact(activity).getMainPhoneNumber());
                smsManager.sendTextMessage(CallManager.getDisplayContact(activity).getMainPhoneNumber(), null, msg, null, null);
                Toast.makeText(activity, "Message Sent", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(activity, "Oh shit I can't send the message... Sorry", Toast.LENGTH_LONG).show();
                e.printStackTrace();
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
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
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
        if (isToOpen) {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    /**
     * Returns a list of the recent calls
     *
     * @param context
     * @return ArrayList<RecentCall>
     */
    public ArrayList<RecentCall> getRecentCalls(Context context) {

        ArrayList<RecentCall> recentCalls = new ArrayList<RecentCall>();

        Uri queryUri = android.provider.CallLog.Calls.CONTENT_URI;

        final String[] PROJECTION = new String[]{
                ContactsContract.Contacts._ID,
                CallLog.Calls._ID,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION};

        String sortOrder = String.format("%s limit 500 ", CallLog.Calls.DATE + " DESC");

        Cursor cursor = context.getContentResolver().query(queryUri, PROJECTION, null, null, sortOrder);

        int title = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);

        while (cursor.moveToNext()) {

            // Get the details
            String callerName = cursor.getString(title);
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            Date callDate = new Date(cursor.getLong(date));
            String callDuration = cursor.getString(duration);

            int callTypeCode = Integer.parseInt(callType);

            RecentCall recentCall = new RecentCall(context, phNumber, callTypeCode, callDuration, callDate);
            recentCalls.add(recentCall);
        }
        cursor.close();
        return recentCalls;
    }

    /**
     * Checks wither the text given is numeric or not
     *
     * @param text
     * @return true/false
     */
    public static boolean checkNumeric(String text) {
        boolean numeric = true;

        try {
            Double num = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            numeric = false;
        }

        return numeric;
    }

    /**
     * Returns only the numbers from a string (removes special characters and spaces
     * @param string
     * @return string
     */
    public static String getOnlyNumbers(String string) {
        return string.replaceAll("[^0-9]", "");
    }
}