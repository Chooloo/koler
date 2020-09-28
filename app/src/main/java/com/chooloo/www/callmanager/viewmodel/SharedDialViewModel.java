package com.chooloo.www.callmanager.viewmodel;

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
        mNumber.setValue(null);

        mIsOutOfFocus = new MutableLiveData<>();
        mIsOutOfFocus.setValue(false);

        mIsTopOpened = new MutableLiveData<>();
        mIsTopOpened.setValue(true);
    }

    public MutableLiveData<String> getNumber() {
        if (mNumber.getValue() == "") mNumber.setValue(null);
        return mNumber;
    }

    public void setNumber(String number) {
        if (number == "") mNumber.setValue(null);
        else mNumber.setValue(number);
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
