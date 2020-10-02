package com.chooloo.www.callmanager.database.entity;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.chooloo.www.callmanager.util.Utilities;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.COLUMN_ID;
import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.COLUMN_NAME;
import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.COLUMN_NUMBER;
import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.COLUMN_STARRED;
import static com.chooloo.www.callmanager.cursorloader.ContactsCursorLoader.COLUMN_THUMBNAIL;

@Entity(tableName = "contact_table",
        indices = {@Index("list_id")},
        foreignKeys = @ForeignKey(entity = CGroup.class,
                parentColumns = "list_id",
                childColumns = "list_id",
                onDelete = ForeignKey.CASCADE))

public class Contact implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contact_id")
    long contactId;

    @ColumnInfo(name = "list_id")
    private long listId;

    @ColumnInfo(name = "full_name")
    private String name;

    @ColumnInfo(name = "phone_numbers")
    @NonNull
    private List<String> phoneNumbers;

    @Ignore
    private String photoUri; // No need to save this to the database

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    /**
     * Contact constructor
     * Accepts a name and a list of numbers (without an image)
     *
     * @param name
     * @param phoneNumbers
     */
    public Contact(String name, @NonNull List<String> phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    /**
     * Get only name and phone number
     * Add the phoneNumber for a list of phone numbers for the sake of consistancy
     *
     * @param name
     * @param phoneNumber
     */
    public Contact(String name, @Nullable String phoneNumber) {
        this.name = name;
        this.phoneNumbers = new ArrayList<String>();
        this.phoneNumbers.add(phoneNumber);
    }

    /**
     * Contact constructor
     * Accepts a name, a list of numbers and an image
     *
     * @param name
     * @param phoneNumbers
     * @param photoUri
     */
    @Ignore
    public Contact(String name, @NonNull List<String> phoneNumbers, String photoUri) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.photoUri = photoUri;
    }

    /**
     * Contact constructor
     * Accepts a name, one phone number and an image
     *
     * @param name        the contact's name
     * @param phoneNumber the contact's phone number
     * @param photoUri    the contact's image
     */
    @Ignore
    public Contact(String name, @NonNull String phoneNumber, @Nullable String photoUri) {
        this.name = name;
        this.photoUri = photoUri;
        this.phoneNumbers = new ArrayList<>();
        this.phoneNumbers.add(phoneNumber);
    }

    /**
     * Contact constructor
     * Accepts a name, one phone number and an image
     *
     * @param id          the contact's id
     * @param name        the contact's name
     * @param phoneNumber the contact's phone number
     * @param photoUri    the contact's image
     */
    @Ignore
    public Contact(long id, String name, @NonNull String phoneNumber, @Nullable String photoUri) {
        this.contactId = id;
        this.name = name;
        this.photoUri = photoUri;
        this.phoneNumbers = new ArrayList<>();
        this.phoneNumbers.add(phoneNumber);
    }


    /**
     * Contact constructor
     * Accepts a cursor from with the function loads the content by itself
     *
     * @param cursor
     */
    @Ignore
    public Contact(Cursor cursor) {
        this.contactId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        this.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        this.photoUri = cursor.getString(cursor.getColumnIndex(COLUMN_THUMBNAIL));
        this.phoneNumbers = new ArrayList<>();
        this.phoneNumbers.add(cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)));
        this.isFavorite = "1".equals(cursor.getString(cursor.getColumnIndex(COLUMN_STARRED)));
    }

    /**
     * Returns the contact's id
     *
     * @return the contact's id
     */
    public long getContactId() {
        return contactId;
    }

    /**
     * Returns the contact's name
     *
     * @return String of the name
     */
    @NonNull
    public String getName() {
        return this.name;
    }

    /**
     * Returns all the phone numbers of a contact
     *
     * @return List<String>
     */
    @NonNull
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * Returns the contact's main phone number
     *
     * @return String
     */
    public String getMainPhoneNumber() {
        if (phoneNumbers.isEmpty()) return null;
        String phoneNumber = phoneNumbers.get(0);

        // Try decoding it just in case
        try {
            phoneNumber = java.net.URLDecoder.decode(phoneNumber, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // The number cant be decoded so its probably not needed anyway
        }

        return phoneNumber;
    }

    /**
     * Returns the contact's image (Uri)
     *
     * @return String
     */
    public String getPhotoUri() {
        return photoUri;
    }

    /**
     * Returns the contact's list id
     *
     * @return long
     */
    public long getListId() {
        return listId;
    }

    /**
     * Returns wither the contact is a favorite contact
     *
     * @return
     */
    public boolean getIsFavorite() {
        return isFavorite;
    }

    /**
     * Sets the contact's id by a given id
     *
     * @param contactId
     */
    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    /**
     * Sets the contact's name by a given String
     *
     * @param name
     */
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * Sets the contact's phone numbers by a given list of strings
     *
     * @param phoneNumbers
     */
    public void setPhoneNumbers(@NonNull List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    /**
     * Sets the contact's image by a given image (String)
     *
     * @param photoUri
     */
    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    /**
     * Sets the contact's list id by a given number
     *
     * @param listId
     */
    public void setListId(long listId) {
        this.listId = listId;
    }

    /**
     * Makes the contact favorite/not favorite
     *
     * @param isFavorite
     * @return
     */
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    /**
     * Returns the contact's details in a string
     *
     * @return a string representing the contact
     */
    @NonNull
    @Override
    public String toString() {
        return String.format(Utilities.sLocale, "id: %d, list_id: %d, name: %s, numbers: %s", contactId, listId, name, this.phoneNumbers.toString());
    }

    /**
     * Check if self equals a given Contact object
     *
     * @param obj
     * @return boolean is equals / not
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (super.equals(obj)) return true;
        if (!(obj instanceof Contact)) return false;
        Contact c = (Contact) obj;
        return (name.equals(c.getName()) && phoneNumbers.equals(c.getPhoneNumbers()));
    }
}
