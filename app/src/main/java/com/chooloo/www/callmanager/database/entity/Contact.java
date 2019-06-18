package com.chooloo.www.callmanager.database.entity;

import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.chooloo.www.callmanager.util.Utilities;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "contact_table",
        indices = {@Index("list_id")},
        foreignKeys = @ForeignKey(entity = CGroup.class,
                parentColumns = "list_id",
                childColumns = "list_id",
                onDelete = ForeignKey.CASCADE))

public class Contact {

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

    @Ignore private String photoUri; // No need to save this to the database

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

    @Ignore
    public Contact(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME_PRIMARY));
        this.photoUri = cursor.getString(cursor.getColumnIndex(Phone.PHOTO_THUMBNAIL_URI));
        this.phoneNumbers = new ArrayList<>();
        this.phoneNumbers.add(cursor.getString(cursor.getColumnIndex(Phone.NUMBER)));
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
     * Sets the contact's id by a given id
     *
     * @param contactId
     */
    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    /**
     * Returns the contact's name
     *
     * @return String of the name
     */
    @NonNull
    public String getName() {
        return name;
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
     * Returns all the phone numbers of a contact
     *
     * @return List<String>
     */
    @NonNull
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
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
     * Returns the contact's main phone number
     *
     * @return String
     */
    public String getMainPhoneNumber() {
        if (phoneNumbers.isEmpty()) return null;
        return phoneNumbers.get(0);
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
     * Sets the contact's list id by a given number
     *
     * @param listId
     */
    public void setListId(long listId) {
        this.listId = listId;
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
     * Sets the contact's image by a given image (String)
     *
     * @param photoUri
     */
    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (super.equals(obj)) return true;
        if (obj instanceof Contact) {
            Contact c = (Contact) obj;
            return (name.equals(c.getName()) &&
                    phoneNumbers.equals(c.getPhoneNumbers()));
        }
        return false;
    }
}
