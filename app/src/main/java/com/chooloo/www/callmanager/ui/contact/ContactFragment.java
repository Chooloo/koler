package com.chooloo.www.callmanager.ui.contact;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chooloo.www.callmanager.databinding.FragmentContactBinding;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.service.CallManager;
import com.chooloo.www.callmanager.ui.base.BaseFragment;
import com.chooloo.www.callmanager.util.ContactUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.chooloo.www.callmanager.util.AnimationUtils.animateViews;

public class ContactFragment extends BaseFragment implements ContactMvpView {

    public static final String CONTACT_ARG = "contact";

    private ContactPresenter<ContactMvpView> mPresenter;
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
        mPresenter.detach();
    }

    @Override
    public void onSetup() {
        mPresenter = new ContactPresenter<>();
        mPresenter.attach(this);

        // contact details
        mContact = (Contact) getArgsSafely().getSerializable(CONTACT_ARG);
        binding.contactTextName.setText(mContact.name);
        binding.contactTextNumber.setText(mContact.getNumber());
        binding.contactFavoriteIcon.setVisibility(mContact.getStarred() ? VISIBLE : GONE);
        if (mContact.getPhotoUri() != null) {
            binding.contactImage.setImageURI(Uri.parse(mContact.getPhotoUri()));
        }

        // click listeners
        binding.contactButtonCall.setOnClickListener(view -> mPresenter.onActionCall());
        binding.contactButtonSms.setOnClickListener(view -> mPresenter.onActionSms());
        binding.contactButtonEdit.setOnClickListener(view -> mPresenter.onActionEdit());
        binding.contactButtonDelete.setOnClickListener(view -> mPresenter.onActionDelete());

        animateLayout();
    }

    @Override
    public void callContact() {
        CallManager.call(activity, mContact.getNumber());
    }

    @Override
    public void smsContact() {
        ContactUtils.smsContact(activity, mContact);
    }

    @Override
    public void editContact() {
        ContactUtils.editContact(activity, mContact);
    }

    @Override
    public void openContact() {
        ContactUtils.openContact(activity, mContact);
    }

    @Override
    public void deleteContact() {
        ContactUtils.deleteContact(activity, mContact);
    }

    @Override
    public void animateLayout() {
        animateViews(new View[]{
                binding.contactImage,
                binding.contactTextName,
                binding.contactActionsLayout
        }, 130, true);
    }
}
