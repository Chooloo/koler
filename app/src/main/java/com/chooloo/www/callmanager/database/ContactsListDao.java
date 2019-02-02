package com.chooloo.www.callmanager.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ContactsListDao {

    @Insert
    void insert (ContactsList list);

    @Query("DELETE FROM contacts_list_table")
    int deleteAll();

    @Query("DELETE FROM contacts_list_table WHERE name LIKE :name")
    int deleteByName(String name);

    @Query("SELECT * from contacts_list_table WHERE name LIKE :name")
    LiveData<List<ContactsList>> getContactsListByName(String name);

    @Query("SELECT * from contacts_list_table ORDER BY list_id ASC")
    LiveData<List<ContactsList>> getAllContactsLists();
}