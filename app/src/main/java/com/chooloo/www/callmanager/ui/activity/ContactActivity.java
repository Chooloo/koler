package com.chooloo.www.callmanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.ui.fragment.RecentsFragment;
import com.chooloo.www.callmanager.util.ThemeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ContactActivity extends AbsThemeActivity {

    public static final String CONTACT_INTENT_ID = "CONTACT_INTENT";

    RecentsFragment mRecentsFragment;
    Contact mContact;

    @BindView(R.id.recents_section)
    CardView mRecentsSection;
    @BindView(R.id.contact_name)
    TextView mNameView;

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
            Toast.makeText(this, "Got a contact with name: " + mContact.getName(), Toast.LENGTH_LONG).show();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contact_recents_frame, mRecentsFragment).commit();
            toggleRecentsSection(true);
            showContactDetails(true);
            Timber.i("CONTACT NOT NULL");
        } else {
            toggleRecentsSection(false);
            showContactDetails(false);
        }

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

    private void toggleRecentsSection(boolean toggle) {
        mRecentsSection.setVisibility(toggle ? View.VISIBLE : View.GONE);
    }

    private void showContactDetails(boolean known) {
        String name = known ? mContact.getName() : getString(R.string.unknown);
        mNameView.setText(name);
    }
}