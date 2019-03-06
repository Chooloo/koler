package com.chooloo.www.callmanager.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SharedDialViewModel extends AndroidViewModel {

    private MutableLiveData<String> mNumber;

    public SharedDialViewModel(@NonNull Application application) {
        super(application);
        mNumber = new MutableLiveData<>();
        mNumber.setValue("");
    }

    public void setNumber(String number) {
        mNumber.setValue(number);
    }

    public MutableLiveData<String> getNumber() {
        return mNumber;
    }
}
