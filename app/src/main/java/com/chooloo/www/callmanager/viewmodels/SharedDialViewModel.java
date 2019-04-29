package com.chooloo.www.callmanager.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SharedDialViewModel extends AndroidViewModel {

    private MutableLiveData<String> mNumber;
    private MutableLiveData<Boolean> mIsOutOfFocus;
    private MutableLiveData<Boolean> mIsTopOpened;

    public SharedDialViewModel(@NonNull Application application) {
        super(application);
        mNumber = new MutableLiveData<>();
        mNumber.setValue("");

        mIsOutOfFocus = new MutableLiveData<>();
        mIsOutOfFocus.setValue(false);

        mIsTopOpened = new MutableLiveData<>();
        mIsTopOpened.setValue(true);
    }

    public MutableLiveData<String> getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber.setValue(number);
    }

    public MutableLiveData<Boolean> getIsOutOfFocus() {
        return mIsOutOfFocus;
    }

    public void setIsOutOfFocus(boolean isOutOfFocus) {
        mIsOutOfFocus.setValue(isOutOfFocus);
    }

    public MutableLiveData<Boolean> getIsTopOpened() {
        return mIsTopOpened;
    }

    public void setIsTopOpened(boolean isTopOpened) {
        mIsTopOpened.setValue(isTopOpened);
    }
}
