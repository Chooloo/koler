package com.chooloo.www.callmanager.ui.contact;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.base.BasePresenter;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.Utilities;

import timber.log.Timber;

import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ContactPresenter<V extends ContactContract.View> extends BasePresenter<V> implements ContactContract.Presenter<V> {

    @Override
    public void onBackButtonPressed() {
        mView.onBackPressed();
    }

    @Override
    public void onActionCall() {
        mView.actionCall();
    }

    @Override
    public void onActionSms() {
        mView.actionSms();
    }

    @Override
    public void onActionEdit() {
        mView.actionDelete();
    }

    @Override
    public void onActionInfo() {
        mView.actionInfo();
    }

    @Override
    public void onActionDelete() {
        mView.actionDelete();
    }

    @Override
    public void onActionFav() {
        mView.actionFav();
    }
}
