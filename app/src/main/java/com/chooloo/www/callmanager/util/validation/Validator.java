package com.chooloo.www.callmanager.util.validation;

import android.util.Patterns;

public class Validator {

    /**
     * Validates the given String as a name
     *
     * @param name
     * @return boolean
     */
    public static boolean validateName(String name) {
        return !name.isEmpty();
    }

    /**
     * Validate the given String as a column index
     *
     * @param index
     * @return boolean
     */
    public static boolean validateColumnIndex(String index) {
        if (index.isEmpty()) return false;
        else {
            try {
                Integer.parseInt(index);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
    }

    /**
     * Validates the given String as a phone number
     *
     * @param phoneNumber
     * @return boolean
     */
    public static boolean validatePhoneNumber(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }
}
