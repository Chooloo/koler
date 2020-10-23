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

    private Contact mContact;
    private Activity mActivity;

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    @Override
    public void setActivity() {
        mActivity = mView.getActivity();
    }

    @Override
    public void onBackButtonPressed() {
        mView.onBackPressed();
    }

    @Override
    public void onActionCall() {
        Timber.i("MAIN PHONE NUMBER: %s", mContact.getMainPhoneNumber());
        CallManager.call(mActivity, mContact.getMainPhoneNumber());
    }

    @Override
    public void onActionSms() {
        Utilities.openSmsWithNumber(mActivity, mContact.getMainPhoneNumber());
    }

    @Override
    public void onActionEdit() {
        ContactUtils.openContactToEdit(mActivity, mContact.getContactId());
    }

    @Override
    public void onActionInfo() {
        ContactUtils.openContact(mActivity, mContact.getContactId());
    }

    @Override
    public void onActionDelete() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CONTACTS) == PERMISSION_GRANTED) {
            ContactUtils.deleteContact(mActivity, mContact.getContactId());
        } else {
            Toast.makeText(mContext, "I dont have the permission", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActionFav() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            if (mContact.getIsFavorite()) {
                mContact.setIsFavorite(false);
                mView.toggleFavIcon(true);
                ContactUtils.setContactIsFavorite(mActivity, Long.toString(mContact.getContactId()), false);
            } else {
                mContact.setIsFavorite(true);
                mView.toggleFavIcon(false);
                ContactUtils.setContactIsFavorite(mActivity, Long.toString(mContact.getContactId()), true);
            }
        } else {
            PermissionUtils.askForPermissions(mActivity, new String[]{WRITE_CONTACTS});
            Toast.makeText(mContext, "I dont have the permission to do that :(", Toast.LENGTH_LONG).show();
        }
    }
}
