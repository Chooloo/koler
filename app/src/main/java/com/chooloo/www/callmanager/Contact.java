package com.chooloo.www.callmanager;

import androidx.annotation.NonNull;

public class Contact {

    // Variables
    private String name;
    private String phoneNumber;
    private String photoUri;

    // Constructor
    public Contact(String name, String phoneNumber, String photoUri) {
        this.name = name;
        this.photoUri = photoUri;
        String lastPhoneNumber = phoneNumber;
        if (phoneNumber != null) { // Checks for unusual occurrences of a number
            if (lastPhoneNumber.contains("+972"))
                lastPhoneNumber = lastPhoneNumber.replace("+972", "0");
            if (lastPhoneNumber.contains(" ")) lastPhoneNumber = lastPhoneNumber.replace(" ", "");
            if (lastPhoneNumber.contains("-")) lastPhoneNumber = lastPhoneNumber.replace("-", "");
        }
        this.phoneNumber = lastPhoneNumber;
    }

    // -- Getters -- //

    /**
     * Returns the contact's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the contact's number
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Returns the photoUri of the caller / other side contact
     *
     * @return
     */
    public String getPhotoUri() {
        return photoUri;
    }

    /**
     * Returns the contact's details in a string
     *
     * @return a string representing the contact
     */
    @NonNull
    @Override
    public String toString() {
        return "Name: " + this.name + " Number: " + this.phoneNumber;
    }
}
