package com.chooloo.www.callmanager.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.fragment.RecentsFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.ThemeUtils;
import com.chooloo.www.callmanager.util.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ContactActivity extends AbsThemeActivity {

    public static final String CONTACT_INTENT_ID = "CONTACT_INTENT";

    RecentsFragment mRecentsFragment;
    Contact mContact;

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
    @BindView(R.id.subtitle_recents) TextView mRecentsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setThemeType(ThemeUtils.TYPE_TRANSPARENT_STATUS_BAR);
        setThemeType(ThemeUtils.TYPE_NO_ACTION_BAR);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        getIntentContact();

        if (mContact != null) {
            mRecentsFragment = new RecentsFragment(this, mContact.getMainPhoneNumber(), null);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contact_recents_frame, mRecentsFragment).commit();
            toggleRecentsSection(true);
            showContactDetails(true);
            setActionButtons();
        } else {
            toggleRecentsSection(false);
            showContactDetails(false);
        }
    }

    @OnClick(R.id.contact_button_call)
    public void actionCall(View view) {
        Timber.i("MAIN PHONE NUMBER: %s", mContact.getMainPhoneNumber());
        CallManager.call(this, mContact.getMainPhoneNumber());
    }

    @OnClick(R.id.contact_button_sms)
    public void actionSms(View view) {
        Utilities.openSmsWithNumber(this, mContact.getMainPhoneNumber());
    }

    @OnClick(R.id.contact_button_edit)
    public void actionEdit(View view) {
        ContactUtils.openContactToEdit(this, mContact.getContactId());
    }

    @OnClick(R.id.contact_button_info)
    public void actionInfo(View view) {
        ContactUtils.openContact(this, mContact.getContactId());
    }

    @OnClick(R.id.contact_button_delete)
    public void actionDelete(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PERMISSION_GRANTED) {
            ContactUtils.deleteContact(this, mContact.getContactId());
        } else {
            Toast.makeText(this, "I dont have the permission", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.contact_button_fav)
    public void actionFav(ImageView view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            if (mContact.getIsFavorite()) {
                mContact.setIsFavorite(false);
                view.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_outline_black_24dp));
                ContactUtils.setContactIsFavorite(this, Long.toString(mContact.getContactId()), false);
            } else {
                mContact.setIsFavorite(true);
                view.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp));
                ContactUtils.setContactIsFavorite(this, Long.toString(mContact.getContactId()), true);
            }
        } else {
            PermissionUtils.askForPermission(this, WRITE_CONTACTS);
            Toast.makeText(this, "I dont have the permission to do that :(", Toast.LENGTH_LONG).show();
        }
    }

    private void showContactDetails(boolean known) {
        mNameView.setText(known ? mContact.getName() : getString(R.string.unknown));
        mNumberView.setText(known ? Utilities.formatPhoneNumber(mContact.getMainPhoneNumber()) : null);
        setContactImage();
    }

    private void setContactImage() {
        if (mContact.getPhotoUri() == null || mContact.getPhotoUri().isEmpty()) {
            mImage.setVisibility(View.GONE);
            mImagePlaceholder.setVisibility(View.VISIBLE);
        } else {
            mImage.setVisibility(View.VISIBLE);
            mImagePlaceholder.setVisibility(View.GONE);
            mImage.setImageURI(Uri.parse(mContact.getPhotoUri()));
        }
    }

    public void setActionButtons() {
        mActionInfo.setVisibility(View.VISIBLE);
        mActionEdit.setVisibility(View.VISIBLE);
        if (mContact.getIsFavorite())
            mActionFav.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp));
    }

    private void toggleRecentsSection(boolean toggle) {
        mRecentsTitle.setVisibility(toggle ? View.VISIBLE : View.GONE);
    }

    /**
     * Set mContact to be the contact from the incoming intent
     * (If there is a proper intent)
     */
    private void getIntentContact() {
        Intent intent = getIntent();
        try {
            mContact = (Contact) intent.getSerializableExtra(CONTACT_INTENT_ID);
        } catch (ClassCastException e) {
            e.printStackTrace();
            Toast.makeText(this, "A problem occurred when trying to get contact :(", Toast.LENGTH_SHORT).show();
            mContact = null;
        }
    }

}