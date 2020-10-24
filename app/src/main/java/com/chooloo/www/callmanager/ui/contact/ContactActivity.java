package com.chooloo.www.callmanager.ui.contact;

import android.Manifest;
import android.app.Activity;
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
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.base.BaseThemeActivity;
import com.chooloo.www.callmanager.ui2.fragment.RecentsFragment;
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
import timber.log.Timber;

import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ContactActivity extends BaseThemeActivity implements ContactContract.View {

    public static final String CONTACT_INTENT_ID = "CONTACT_INTENT";

    private ContactContract.Presenter<ContactContract.View> mPresenter;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setThemeType(ThemeUtils.TYPE_TRANSPARENT_STATUS_BAR);
        setThemeType(ThemeUtils.TYPE_NO_ACTION_BAR);
        setContentView(R.layout.activity_contact);

        ButterKnife.bind(this);

        mPresenter = new ContactPresenter();
        mPresenter.bind(this, getLifecycle());
        mPresenter.subscribe(this);

        setUp();
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
        setContactFromIntent();

        if (mContact != null) {
            mRecentsFragment = new RecentsFragment(this, mContact.getMainPhoneNumber(), null);
            mRecentsFragment.setOnLoadFinishListener(this::handleNoRecents);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.recents_section_frame, mRecentsFragment).commit();

            mActionInfo.setVisibility(VISIBLE);
            mActionEdit.setVisibility(VISIBLE);
            if (mContact.getIsFavorite())
                mActionFav.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp));

            mNameView.setText(mContact.getName());
            mNumberView.setText(PhoneNumberUtils.formatPhoneNumber(this, mContact.getMainPhoneNumber()));

            if (mContact.getPhotoUri() == null || mContact.getPhotoUri().isEmpty()) {
                mImage.setVisibility(GONE);
                mImagePlaceholder.setVisibility(VISIBLE);
            } else {
                mImage.setVisibility(VISIBLE);
                mImagePlaceholder.setVisibility(GONE);
                mImage.setImageURI(Uri.parse(mContact.getPhotoUri()));
            }

            mRecentsLayout.setVisibility(VISIBLE);
        } else {
            mNameView.setText(getString(R.string.unknown));
            mNumberView.setText(null);

            mRecentsLayout.setVisibility(GONE);
        }
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
            Toast.makeText(this, "A problem occurred when trying to get contact :(", Toast.LENGTH_SHORT).show();
            mContact = null;
        }
    }

    @Override
    public void handleNoRecents() {
        if (mRecentsFragment.size() == 0) {
            mRecentsEmpty.setVisibility(View.VISIBLE);
            mRecentsTitle.setVisibility(View.GONE);
            mRecentsFrame.setVisibility(View.GONE);
        }
    }

}
