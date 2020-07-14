package com.chooloo.www.callmanager.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.apache.poi.sl.usermodel.ObjectMetaData;

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
