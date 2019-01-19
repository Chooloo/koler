package com.chooloo.www.callmanager;

import androidx.annotation.NonNull;

public class Contact {

    // Variables
    private String mContactName;
    private String mContactNumber;

    // Constructor
    public Contact(String name, String number) {
        this.mContactName = name;
        this.mContactNumber = number;
    }

    // -- Getters -- //

    /**
     * Returns the contact's name
     *
     * @return mContactName
     */
    public String getContactName() {
        return this.mContactName;
    }

    /**
     * Returns the contact's number
     *
     * @return mContactNumber
     */
    public String getContactNumber() {
        return this.mContactNumber;
    }

    /**
     * Returns the contact's details in a string
     *
     * @return a string representing the contact
     */
    @NonNull
    @Override
    public String toString() {
        return "Name: " + this.mContactName + " Number: " + this.mContactNumber;
    }
}
