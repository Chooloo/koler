package com.chooloo.www.callmanager.database.entity;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class CGroupAndItsContacts {

    @Embedded private CGroup cgroup;
    @Relation(parentColumn = "list_id", entityColumn = "list_id") private List<Contact> contacts;

    public CGroupAndItsContacts(CGroup cgroup) {
        this.cgroup = cgroup;
    }

    public CGroup getCgroup() {
        return cgroup;
    }

    public void setCgroup(CGroup cgroup) {
        this.cgroup = cgroup;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
