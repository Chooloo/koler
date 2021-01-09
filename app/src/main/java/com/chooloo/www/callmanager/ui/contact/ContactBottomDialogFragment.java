package com.chooloo.www.callmanager.ui.contact;

import android.os.Bundle;

import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.ui.base.BaseBottomSheetDialogFragment;

public class ContactBottomDialogFragment extends BaseBottomSheetDialogFragment {

    public static final String TAG = "contact_bottom_dialog_fragment";
    public static final String ARG_CONTACT = "contact";

    private ContactFragment mContactFragment;

    public static ContactBottomDialogFragment newInstance(Contact contact) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACT, contact);
        ContactBottomDialogFragment fragment = new ContactBottomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSetup() {
        mContactFragment = ContactFragment.newInstance((Contact) getArgsSafely().getSerializable(ARG_CONTACT));
        putFragment(mContactFragment);
    }
}
