package com.chooloo.www.callmanager.ui.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CurrentFragmentViewModel extends AndroidViewModel {

    public final static String mContactsFragment = "CONTACTS_FRAGMENT";
    public final static String mCGroupsFragment = "CGROUPS_FRAGMENT";

    private MutableLiveData<String> mCurrentFragment;

    public CurrentFragmentViewModel(@NonNull Application application) {
        super(application);

        mCurrentFragment = new MutableLiveData<>();
        mCurrentFragment.setValue(mContactsFragment);
    }

    public MutableLiveData<String> getCurrentFragment() {
        return mCurrentFragment;
    }

    public void setCurrentFragment(String currentFragment) {
        mCurrentFragment.setValue(currentFragment);
    }
}
