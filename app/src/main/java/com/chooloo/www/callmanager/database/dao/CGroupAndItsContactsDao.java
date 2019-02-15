package com.chooloo.www.callmanager.database.dao;

import com.chooloo.www.callmanager.database.entity.CGroupAndItsContacts;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface CGroupAndItsContactsDao {

    @Query("SELECT * from cgroup_table")
    @Transaction
    LiveData<List<CGroupAndItsContacts>> getAllCGroupsAndTheirContacts();
}
