package com.chooloo.www.callmanager;

import androidx.annotation.NonNull;

public class Contact {

    public static final Contact UNKNOWN = new Contact("Unknown", null, null);
    public static final Contact VOICEMAIL = new Contact("Voicemail", null, null);

    // Variables
    private String name;
    private String phoneNumber;
    private String photoUri;

    // Constructor

    public Contact(String name, String phoneNumber, String photoUri) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photoUri = photoUri;
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
