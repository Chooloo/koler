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
        if (phoneNumber != null) {
            if (phoneNumber.contains("+972")) this.phoneNumber = phoneNumber.replace("+972", "0");
            if (phoneNumber.contains(" ")) this.phoneNumber = phoneNumber.replace(" ", "");
            if (phoneNumber.contains("-")) this.phoneNumber = phoneNumber.replace("-", "n");
        } else this.phoneNumber = phoneNumber;
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
