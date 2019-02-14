package com.chooloo.www.callmanager.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cgroup_table")
public class CGroup {

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "list_id")
    private long listId;

    @NonNull @ColumnInfo(name = "name")
    private String name;

    public CGroup(@NonNull String name) {
        this.name = name;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
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
        if (obj instanceof CGroup) {
            CGroup cl = (CGroup) obj;
            return name.equals(cl.getName());
        }
        return false;
    }
}
