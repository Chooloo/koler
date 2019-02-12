package com.chooloo.www.callmanager.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ContactDao {

    @Insert
    void insert(Contact contact);

    @Insert
    void insert(List<Contact> contacts);

    @Query("DELETE FROM contact_table")
    void deleteAll();

    @Query("DELETE FROM contact_table WHERE phone_numbers LIKE '%' || :phoneNumber || '%'")
    int deleteByPhoneNumber(String phoneNumber);

    @Query("SELECT * from contact_table WHERE phone_numbers LIKE '%' || :phoneNumber || '%'")
    LiveData<List<Contact>> getContactsByPhoneNumber(String phoneNumber);

    @Query("SELECT * from contact_table ORDER BY contact_id ASC")
    LiveData<List<Contact>> getAllContacts();
}
