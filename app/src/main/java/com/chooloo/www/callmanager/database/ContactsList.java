package com.chooloo.www.callmanager.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts_list_table")
public class ContactsList {

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "list_id") private int listId;

    @NonNull @ColumnInfo(name = "name") private String name;

    public ContactsList(@NonNull String name) {
        this.name = name;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (super.equals(obj)) return true;
        if (obj instanceof ContactsList) {
            ContactsList cl = (ContactsList) obj;
            return name.equals(cl.getName());
        }
        return false;
    }
}
