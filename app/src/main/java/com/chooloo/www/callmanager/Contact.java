package com.chooloo.www.callmanager;

public class Contact {
    private String mContactName;
    private String mContactNumber;

    public Contact(String name, String number) {
        this.mContactName = name;
        this.mContactNumber = number;
    }

    public String getContactName() {
        return this.mContactName;
    }

    public String getContactNumber() {
        return this.mContactNumber;
    }

    public String getContactPrint() {
        return "Name: " + this.mContactName + " Number: " + this.mContactNumber;
    }
}
