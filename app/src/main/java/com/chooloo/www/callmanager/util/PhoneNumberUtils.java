package com.chooloo.www.callmanager.util;

import android.content.Context;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;

import timber.log.Timber;

import static com.chooloo.www.callmanager.util.Utilities.sLocale;
import static com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;
import static com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.NATIONAL;

public class PhoneNumberUtils {

    /**
     * Return a normalized number
     *
     * @param phoneNumber number to normalize
     * @return the number, normalized
     */
    public static String normalizePhoneNumber(String phoneNumber) {
        return PhoneNumberUtil.normalizeDiallableCharsOnly(phoneNumber);
    }


    /**
     * Format a given phone number to a readable string for the user
     * Examples:
     *     9876543210 ->     98765 43210
     *    09876543210 ->    098765 43210
     *  +919876543210 -> +91 98765 43210
     *
     * @param phoneNumber the number to format
     * @return the formatted number
     */
    public static String formatPhoneNumber(Context context, String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }

        return android.telephony.PhoneNumberUtils.formatNumber(phoneNumber, Utilities.getCountry(context));
    }
}
