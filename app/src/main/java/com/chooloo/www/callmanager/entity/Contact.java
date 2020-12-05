package com.chooloo.www.callmanager.entity;

import android.database.Cursor;

import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.util.Utilities;

import java.io.Serializable;

import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.COLUMN_ID;
import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.COLUMN_NAME;
import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.COLUMN_STARRED;
import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.COLUMN_THUMBNAIL;

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

    public Contact(long contactId, String name, String photoUri, boolean starred, String number) {
        this.contactId = contactId;
        this.name = name;
        this.photoUri = photoUri;
        this.starred = starred;
        this.number = number;
    }

    public Contact(Cursor cursor) {
        this.contactId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        this.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        this.photoUri = cursor.getString(cursor.getColumnIndex(COLUMN_THUMBNAIL));
        this.starred = "1".equals(cursor.getString(cursor.getColumnIndex(COLUMN_STARRED)));
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
        if (super.equals(obj)) return true;
        if (!(obj instanceof Contact)) return false;
        Contact c = (Contact) obj;
        return (name.equals(c.name));
    }
}
