package com.chooloo.www.callmanager.util.validation;

public class Validator {

    public static boolean validateName(String name) {
        return !name.isEmpty();
    }

    public static boolean validateColumnIndex(String index) {
        if (index.isEmpty()) return false;
        else {
            try {
                Integer.parseInt(index);
                return true;
            }
            catch (NumberFormatException ex) {
                return false;
            }
        }
    }
}
