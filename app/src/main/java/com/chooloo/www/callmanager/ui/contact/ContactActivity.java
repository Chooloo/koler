package com.chooloo.www.callmanager.ui.contact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.recents.RecentsFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.PhoneNumberUtils;
import com.chooloo.www.callmanager.util.ThemeUtils;
import com.chooloo.www.callmanager.util.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ContactActivity extends BaseActivity implements ContactMvpView {

    public static final String CONTACT_INTENT_ID = "CONTACT_INTENT";

    private ContactPresenter<ContactMvpView> mPresenter;
    private RecentsFragment mRecentsFragment;
    private Contact mContact;

    // Contact Info
    @BindView(R.id.contact_name) TextView mNameView;
    @BindView(R.id.contact_number) TextView mNumberView;
    @BindView(R.id.contact_image_placeholder) ImageView mImagePlaceholder;
    @BindView(R.id.contact_image_photo) CircleImageView mImage;

    // Action Buttons
    @BindView(R.id.contact_button_call) ImageButton mActionCall;
    @BindView(R.id.contact_button_sms) ImageButton mActionSms;
    @BindView(R.id.contact_button_edit) ImageButton mActionEdit;
    @BindView(R.id.contact_button_info) ImageButton mActionInfo;
    @BindView(R.id.contact_button_delete) ImageButton mActionDelete;
    @BindView(R.id.contact_button_fav) ImageButton mActionFav;

    // Recents Section
    @BindView(R.id.recents_section_layout) ConstraintLayout mRecentsLayout;
    @BindView(R.id.recents_section_title) TextView mRecentsTitle;
    @BindView(R.id.recents_section_frame) FrameLayout mRecentsFrame;
    @BindView(R.id.recents_section_empty) TextView mRecentsEmpty;

    @Override
    public int getContentView() {
        return R.layout.activity_contact;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @OnClick(R.id.contact_button_back)
    public void goBack(View view) {
        mPresenter.onBackButtonPressed();
    }

    @OnClick(R.id.contact_button_call)
    public void actionCall(View view) {
        mPresenter.onActionCall();
    }

    @OnClick(R.id.contact_button_sms)
    public void actionSms(View view) {
        mPresenter.onActionSms();
    }

    @OnClick(R.id.contact_button_edit)
    public void actionEdit(View view) {
        mPresenter.onActionEdit();
    }

    @OnClick(R.id.contact_button_info)
    public void actionInfo(View view) {
        mPresenter.onActionInfo();
    }

    @OnClick(R.id.contact_button_delete)
    public void actionDelete(View view) {
        mPresenter.onActionDelete();
    }

    @OnClick(R.id.contact_button_fav)
    public void actionFav(ImageView view) {
        mPresenter.onActionFav();
    }

    @Override
    public void setUp() {
        ButterKnife.bind(this);

        mPresenter = new ContactPresenter<>();
        mPresenter.onAttach(this, getLifecycle());

        setContactFromIntent();

        // set details
        mNumberView.setText(mContact != null ? PhoneNumberUtils.formatPhoneNumber(this, mContact.getMainPhoneNumber()) : null);
        mNameView.setText(mContact != null ? mContact.getName() : getString(R.string.unknown));

        // set actions
        mActionInfo.setVisibility(mContact != null ? VISIBLE : GONE);
        mActionEdit.setVisibility(mContact != null ? VISIBLE : GONE);
        mActionFav.setImageDrawable(mContact != null ? ContextCompat.getDrawable(this, mContact.getIsFavorite() ? R.drawable.ic_star_outline_black_24dp : R.drawable.ic_star_black_24dp) : null);

        // set image
        if (mContact != null) {
            mImage.setVisibility(mContact.getPhotoUri() != null ? VISIBLE : GONE);
            mImagePlaceholder.setVisibility(mContact.getPhotoUri() != null ? GONE : VISIBLE);
            if (mContact.getPhotoUri() != null) {
                mImage.setImageURI(Uri.parse(mContact.getPhotoUri()));
            }
        }

        // set recents fragment
        if (mContact != null) {
            mRecentsFragment = RecentsFragment.newInstance(mContact.getMainPhoneNumber(), null);
            mRecentsFragment.setOnLoadFinishedListener(() -> mPresenter.onRecentsLoadFinished());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.recents_section_frame, mRecentsFragment).commit();
        }
        mRecentsLayout.setVisibility(mContact != null ? VISIBLE : GONE);
    }

    @Override
    public void actionCall() {
        CallManager.call(this, mContact.getMainPhoneNumber());
    }

    @Override
    public void actionSms() {
        Utilities.openSmsWithNumber(this, mContact.getMainPhoneNumber());
    }

    @Override
    public void actionEdit() {
        ContactUtils.openContactToEdit(this, mContact.getContactId());
    }

    @Override
    public void actionInfo() {
        ContactUtils.openContact(this, mContact.getContactId());
    }

    @Override
    public void actionDelete() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PERMISSION_GRANTED) {
            ContactUtils.deleteContact(this, mContact.getContactId());
        } else {
            Toast.makeText(this, "I dont have the permission", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void actionFav() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            if (mContact.getIsFavorite()) {
                mContact.setIsFavorite(false);
                toggleFavIcon(true);
                ContactUtils.setContactIsFavorite(this, Long.toString(mContact.getContactId()), false);
            } else {
                mContact.setIsFavorite(true);
                toggleFavIcon(false);
                ContactUtils.setContactIsFavorite(this, Long.toString(mContact.getContactId()), true);
            }
        } else {
            PermissionUtils.askForPermissions(this, new String[]{WRITE_CONTACTS});
            Toast.makeText(this, "I dont have the permission to do that :(", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void toggleFavIcon(boolean toggle) {
        mActionFav.setImageDrawable(ContextCompat.getDrawable(this, toggle ? R.drawable.ic_star_outline_black_24dp : R.drawable.ic_star_black_24dp));
    }

    @Override
    public void setContactFromIntent() {
        Intent intent = getIntent();
        try {
            mContact = (Contact) intent.getSerializableExtra(CONTACT_INTENT_ID);
        } catch (ClassCastException e) {
            e.printStackTrace();
            showError("A problem occurred when trying to get contact :(");
            mContact = null;
        }
    }

    @Override
    public void handleNoRecents() {
        if (mRecentsFragment.getSize() == 0) {
            mRecentsEmpty.setVisibility(View.VISIBLE);
            mRecentsTitle.setVisibility(View.GONE);
            mRecentsFrame.setVisibility(View.GONE);
        }
    }

}
