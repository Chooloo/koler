package com.chooloo.www.callmanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SharedIntentViewModel extends AndroidViewModel {

    private MutableLiveData<String> mData;

    public SharedIntentViewModel(@NonNull Application application) {
        super(application);
        mData = new MutableLiveData<>();
        mData.setValue("");

    }

    public MutableLiveData<String> getData() {
        return mData;
    }

    public void setData(String number) {
        mData.setValue(number);
    }

}
