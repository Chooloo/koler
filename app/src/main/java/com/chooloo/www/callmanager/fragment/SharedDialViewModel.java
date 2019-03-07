package com.chooloo.www.callmanager.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SharedDialViewModel extends AndroidViewModel {

    private MutableLiveData<String> mNumber;
    private MutableLiveData<Boolean> mIsOutOfFocus;

    public SharedDialViewModel(@NonNull Application application) {
        super(application);
        mNumber = new MutableLiveData<>();
        mNumber.setValue("");

        mIsOutOfFocus = new MutableLiveData<>();
        mIsOutOfFocus.setValue(false);
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
}
