package com.chooloo.www.callmanager.database.entity;

import com.chooloo.www.callmanager.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table",
        foreignKeys = @ForeignKey(entity = CGroup.class,
                parentColumns = "list_id",
                childColumns = "list_id",
                onDelete = ForeignKey.CASCADE))
public class Contact {

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "contact_id")
    long contactId;

    @ColumnInfo(name = "list_id")
    private long listId;

    @ColumnInfo(name = "full_name")
    private String name;

    @ColumnInfo(name = "phone_numbers")
    @NonNull private List<String> phoneNumbers;

    @Ignore private String photoUri; //No need to save this to the database

    public Contact(String name, @NonNull List<String> phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    @Ignore
    public Contact(String name, @NonNull List<String> phoneNumbers, String photoUri) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.photoUri = photoUri;
    }

    @Ignore
    public Contact(String name, @NonNull String phoneNumber, @Nullable String photoUri) {
        this.name = name;
        this.photoUri = photoUri;

        this.phoneNumbers = new ArrayList<>();
        this.phoneNumbers.add(phoneNumber);
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(@NonNull List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getMainPhoneNumber() {
        return phoneNumbers.get(0);
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getPhotoUri() {
        return photoUri;
    }

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
        return String.format(Utilities.LOCALE, "id: %d, list_id: %d, name: %s, numbers: %s", contactId, listId, name, this.phoneNumbers.toString());
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
