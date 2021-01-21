package com.chooloo.www.callmanager.entity;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.util.Utilities;

import java.io.Serializable;

public class Contact implements Serializable {

    private long contactId;
    private final String name;
    private String photoUri;
    private boolean starred;
    private String number;

    public Contact(@Nullable String name, @Nullable String number) {
        this.name = name;
        this.number = number;
    }

    public Contact(long contactId, String name, String photoUri, boolean starred) {
        this.contactId = contactId;
        this.name = name;
        this.photoUri = photoUri;
        this.starred = starred;
    }

    public static Contact unknownContact() {
        return new Contact(0, "Unknown", null, false);
    }

    public long getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public boolean getStarred() {
        return starred;
    }

    public String getNumber() {
        return number;
    }

    public String toString() {
        return String.format(Utilities.sLocale, "id: %d, name: %s", contactId, name);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Contact && (super.equals(obj) || name.equals(((Contact) obj).name));
    }
}
