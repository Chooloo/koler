package com.chooloo.www.callmanager.database;

import com.chooloo.www.callmanager.database.entity.CGroup;
import com.chooloo.www.callmanager.database.entity.Contact;

import java.util.List;

import androidx.lifecycle.LiveData;

public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public LiveData<List<Contact>> getAllContacts() {
        return mDatabase.getContactDao().getAllContacts();
    }

    public LiveData<List<Contact>> getContactsInList(CGroup list) {
        return mDatabase.getContactDao().getContactsInList(list.getListId());
    }

    public LiveData<List<Contact>> getContactsInList(long listId) {
        return mDatabase.getContactDao().getContactsInList(listId);
    }

    public LiveData<List<CGroup>> getAllCGroups() {
        return mDatabase.getCGroupDao().getAllCGroups();
    }
}
