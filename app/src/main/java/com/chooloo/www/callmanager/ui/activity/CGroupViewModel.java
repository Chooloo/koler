package com.chooloo.www.callmanager.ui.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.chooloo.www.callmanager.database.AppDatabase;
import com.chooloo.www.callmanager.database.DataRepository;
import com.chooloo.www.callmanager.database.entity.CGroup;
import com.chooloo.www.callmanager.database.entity.Contact;

import java.util.List;

public class CGroupViewModel extends AndroidViewModel {

    private DataRepository mRepository;

    private long mListId;
    private LiveData<List<Contact>> mContacts;

    /**
     * Constructor
     *
     * @param application
     */
    public CGroupViewModel(@NonNull Application application) {
        super(application);
        mRepository = DataRepository.getInstance(AppDatabase.getDatabase(application.getApplicationContext()));
    }

    /**
     * Sets the list id by a given long
     *
     * @param listId
     */
    public void setListId(long listId) {
        mListId = listId;
        mContacts = mRepository.getContactsInList(listId);
    }

    /**
     * Returns a list of the contact
     *
     * @return
     */
    public LiveData<List<Contact>> getContacts() {
        return mContacts;
    }

    /**
     * Returns a list of the CGroup
     *
     * @return
     */
    public LiveData<List<CGroup>> getCGroup() {
        return mRepository.getCGroup(mListId);
    }
}
