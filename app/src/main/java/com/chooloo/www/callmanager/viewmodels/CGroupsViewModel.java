package com.chooloo.www.callmanager.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.chooloo.www.callmanager.database.AppDatabase;
import com.chooloo.www.callmanager.database.DataRepository;
import com.chooloo.www.callmanager.database.entity.CGroupAndItsContacts;

import java.util.List;

public class CGroupsViewModel extends AndroidViewModel {

    private DataRepository mRepository;
    private LiveData<List<CGroupAndItsContacts>> mContactsLists;

    public CGroupsViewModel(@NonNull Application application) {
        super(application);
        mRepository = DataRepository.getInstance(AppDatabase.getDatabase(application.getApplicationContext()));
        mContactsLists = mRepository.getAllCGroupsAndTheirContacts();
    }

    public LiveData<List<CGroupAndItsContacts>> getContactsLists() {
        return mContactsLists;
    }
}
