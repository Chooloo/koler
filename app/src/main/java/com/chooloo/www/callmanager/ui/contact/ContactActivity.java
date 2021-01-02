package com.chooloo.www.callmanager.ui.contact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.ActivityContactBinding;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.base.BaseActivity;
import com.chooloo.www.callmanager.ui.recents.RecentsFragment;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;

import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ContactActivity extends BaseActivity implements ContactMvpView {

    public static final String CONTACT_INTENT_ID = "CONTACT_INTENT";

    private ContactPresenter<ContactMvpView> mPresenter;
    private RecentsFragment mRecentsFragment;
    private Contact mContact;
    private ActivityContactBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }


    @Override
    public void setUp() {
        mPresenter = new ContactPresenter<>();
        mPresenter.onAttach(this);

        setContactFromIntent();

        // set details
//        binding.contactNumber.setText(mContact != null ? PhoneNumberUtils.formatPhoneNumber(this, mContact.getMainPhoneNumber()) : null);
        binding.contactName.setText(mContact != null ? mContact.getName() : getString(R.string.unknown));

        // set actions
        binding.contactActionButtons.contactButtonInfo.setVisibility(mContact != null ? VISIBLE : GONE);
        binding.contactActionButtons.contactButtonEdit.setVisibility(mContact != null ? VISIBLE : GONE);
        binding.contactButtonFav.setImageDrawable(mContact != null ? ContextCompat.getDrawable(this, mContact.getStarred() ? R.drawable.ic_star_outline_black_24dp : R.drawable.ic_star_black_24dp) : null);

        // set image
        if (mContact != null) {
            binding.contactImagePhoto.setVisibility(mContact.getPhotoUri() != null ? VISIBLE : GONE);
            binding.contactImagePlaceholder.setVisibility(mContact.getPhotoUri() != null ? GONE : VISIBLE);
            if (mContact.getPhotoUri() != null) {
                binding.contactImagePhoto.setImageURI(Uri.parse(mContact.getPhotoUri()));
                binding.contactImagePhoto.setImageURI(Uri.parse(mContact.getPhotoUri()));
            }
        }

        // set recents fragment
        if (mContact != null) {
//            mRecentsFragment = RecentsFragment.newInstance(mContact.getMainPhoneNumber(), null);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.recents_section_frame, mRecentsFragment).commit();
        }
        binding.recentsSectionLayout.setVisibility(mContact != null ? VISIBLE : GONE);

        binding.contactButtonFav.setOnClickListener(view -> mPresenter.onActionFav());
        binding.contactButtonBack.setOnClickListener(view -> mPresenter.onBackButtonPressed());
        binding.contactActionButtons.contactButtonSms.setOnClickListener(view -> mPresenter.onActionSms());
        binding.contactActionButtons.contactButtonCall.setOnClickListener(view -> mPresenter.onActionCall());
        binding.contactActionButtons.contactButtonEdit.setOnClickListener(view -> mPresenter.onActionEdit());
        binding.contactActionButtons.contactButtonInfo.setOnClickListener(view -> mPresenter.onActionInfo());
        binding.contactActionButtons.contactButtonDelete.setOnClickListener(view -> mPresenter.onActionDelete());
    }

    @Override
    public void actionCall() {
//        CallManager.call(this, mContact.getMainPhoneNumber());
    }

    @Override
    public void actionSms() {
//        Utilities.openSmsWithNumber(this, mContact.getMainPhoneNumber());
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
            if (mContact.getStarred()) {
//                mContact.setIsFavorite(false);
                toggleFavIcon(true);
                ContactUtils.setContactIsFavorite(this, Long.toString(mContact.getContactId()), false);
            } else {
//                mContact.setIsFavorite(true);
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
        binding.contactButtonFav.setImageDrawable(ContextCompat.getDrawable(this, toggle ? R.drawable.ic_star_outline_black_24dp : R.drawable.ic_star_black_24dp));
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
        if (mRecentsFragment.getItemCount() == 0) {
            binding.recentsSectionEmpty.setVisibility(View.VISIBLE);
            binding.recentsSectionTitle.setVisibility(View.GONE);
            binding.recentsSectionFrame.setVisibility(View.GONE);
        }
    }

}
