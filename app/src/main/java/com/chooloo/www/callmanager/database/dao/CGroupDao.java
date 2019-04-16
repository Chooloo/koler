package com.chooloo.www.callmanager.database.dao;

import com.chooloo.www.callmanager.database.entity.CGroup;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CGroupDao {

    @Insert
    long insert (CGroup list);

    @Query("DELETE FROM cgroup_table")
    int deleteAll();

    @Query("DELETE FROM cgroup_table WHERE name LIKE :name")
    int deleteByName(String name);

    @Query("DELETE FROM cgroup_table WHERE list_id LIKE :listId")
    int deleteById(long listId);

    @Query("SELECT * from cgroup_table WHERE list_id LIKE :listId")
    LiveData<List<CGroup>> getCGroupById(long listId);

    @Query("SELECT * from cgroup_table WHERE name LIKE :name")
    LiveData<List<CGroup>> getCGroupByName(String name);

    @Query("SELECT * from cgroup_table ORDER BY list_id ASC")
    LiveData<List<CGroup>> getAllCGroups();
}