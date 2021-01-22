package com.chooloo.www.callmanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SharedDialViewModel extends AndroidViewModel {

    private MutableLiveData<String> mNumber;

    public SharedDialViewModel(@NonNull Application application) {
        super(application);
        mNumber = new MutableLiveData<>();
        mNumber.setValue(null);
    }

    public MutableLiveData<String> getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        if (number == "") {
            mNumber.setValue(null);
        } else {
            mNumber.setValue(number);
        }
    }
}
