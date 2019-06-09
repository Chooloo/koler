package com.chooloo.www.callmanager.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SharedSearchViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Boolean> mIsFocused;
    private MutableLiveData<Boolean> mIsOpened;

    public SharedSearchViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("");

        mIsFocused = new MutableLiveData<>();
        mIsFocused.setValue(false);

        mIsOpened = new MutableLiveData<>();
        mIsOpened.setValue(false);
    }

    public MutableLiveData<String> getText() {
        return mText;
    }

    public void setText(String text) {
        mText.setValue(text);
    }

    public MutableLiveData<Boolean> getIsFocused() {
        return mIsFocused;
    }

    public void setIsFocused(Boolean isFocused) {
        mIsFocused.setValue(isFocused);
    }

    public MutableLiveData<Boolean> getIsOpened() {
        return mIsOpened;
    }

    public void setIsOpened(Boolean isOpened) {
        mIsOpened.setValue(isOpened);
    }
}
