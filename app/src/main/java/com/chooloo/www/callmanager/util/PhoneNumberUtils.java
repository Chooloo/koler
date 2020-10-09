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
     *
     * @param phoneNumber the number to format
     * @return the formatted number
     */
    public static String formatPhoneNumber(Context context, String phoneNumber) {
        if (phoneNumber == null) return null;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber formattedNumber = null;

        // Check for international number
        try {
            formattedNumber = phoneUtil.parse(phoneNumber.contains("+") ? phoneNumber : "+" + phoneNumber, null);
            return phoneUtil.format(formattedNumber, INTERNATIONAL);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        // Check for national number (User's country as default)
        try {
            formattedNumber = phoneUtil.parse(phoneNumber, Utilities.getCountry(context));
            Timber.i("FORMATTING " + phoneNumber + " -> " + phoneUtil.format(formattedNumber, NATIONAL));
            return phoneUtil.format(formattedNumber, NATIONAL);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return phoneNumber;
    }
}
