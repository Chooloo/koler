package com.chooloo.www.callmanager.ui.contact;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.databinding.FragmentContactBinding;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.ui.recents.RecentsFragment;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;

import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ContactFragment extends BaseFragment implements ContactMvpView {

    public static final String CONTACT_ARG = "contact";

    private ContactPresenter<ContactMvpView> mPresenter;
    private RecentsFragment mRecentsFragment;
    private Contact mContact;
    private FragmentContactBinding binding;


    public static ContactFragment newInstance(Contact contact) {
        Bundle args = new Bundle();
        args.putSerializable(CONTACT_ARG, contact);
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentContactBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDetach();
    }

    @Override
    public void onSetup() {
        mPresenter = new ContactPresenter<>();
        mPresenter.onAttach(this);

        mContact = (Contact) getArgsSafely().getSerializable(CONTACT_ARG);

        // set details
//        binding.contactNumber.setText(mContact != null ? PhoneNumberUtils.formatPhoneNumber(this, mContact.getMainPhoneNumber()) : null);
        binding.contactName.setText(mContact != null ? mContact.getName() : getString(R.string.unknown));

        // set actions
        binding.contactActionButtons.contactButtonInfo.setVisibility(mContact != null ? VISIBLE : GONE);
        binding.contactActionButtons.contactButtonEdit.setVisibility(mContact != null ? VISIBLE : GONE);
        binding.contactButtonFav.setImageDrawable(mContact != null ? ContextCompat.getDrawable(mActivity, mContact.getStarred() ? R.drawable.ic_star_outline_black_24dp : R.drawable.ic_star_black_24dp) : null);

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
            FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.recents_section_frame, mRecentsFragment).commit();
        }
        binding.recentsSectionLayout.setVisibility(mContact != null ? VISIBLE : GONE);

        binding.contactButtonFav.setOnClickListener(view -> mPresenter.onActionFav());
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
        ContactUtils.openContactToEdit(mActivity, mContact.getContactId());
    }

    @Override
    public void actionInfo() {
        ContactUtils.openContact(mActivity, mContact.getContactId());
    }

    @Override
    public void actionDelete() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_CONTACTS) == PERMISSION_GRANTED) {
            ContactUtils.deleteContact(mActivity, mContact.getContactId());
        } else {
            Toast.makeText(mActivity, "I dont have the permission", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void actionFav() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            if (mContact.getStarred()) {
//                mContact.setIsFavorite(false);
                toggleFavIcon(true);
                ContactUtils.setContactIsFavorite(mActivity, Long.toString(mContact.getContactId()), false);
            } else {
//                mContact.setIsFavorite(true);
                toggleFavIcon(false);
                ContactUtils.setContactIsFavorite(mActivity, Long.toString(mContact.getContactId()), true);
            }
        } else {
            PermissionUtils.askForPermissions(this, new String[]{WRITE_CONTACTS});
            Toast.makeText(mActivity, "I dont have the permission to do that :(", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void toggleFavIcon(boolean toggle) {
        binding.contactButtonFav.setImageDrawable(ContextCompat.getDrawable(mActivity, toggle ? R.drawable.ic_star_outline_black_24dp : R.drawable.ic_star_black_24dp));
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
