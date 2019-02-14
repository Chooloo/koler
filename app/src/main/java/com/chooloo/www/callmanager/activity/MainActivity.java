package com.chooloo.www.callmanager.activity;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chooloo.www.callmanager.OnSwipeTouchListener;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.fragment.ContactsFragment;
import com.chooloo.www.callmanager.fragment.DialFragment;
import com.chooloo.www.callmanager.util.ContactsManager;
import com.chooloo.www.callmanager.util.PreferenceUtils;

import java.util.AbstractMap;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;
import static com.chooloo.www.callmanager.util.Utilities.checkStrPermission;

public class MainActivity extends AppBarActivity implements DialFragment.OnDialChangeListener, ContactsFragment.OnContactsChangeListener {

    // Variables
    boolean mIsScrolling;

    // Layouts and Fragments
    @BindView(R.id.appbar) View mAppBar;
    @BindView(R.id.activity_main) ConstraintLayout mMainLayout;
    ViewGroup mDialerLayout;
    DialFragment mDialFragment;
    ContactsFragment mContactsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceUtils.getInstance(this);

        // Bind variables
        ButterKnife.bind(this);

        // Init timber
        Timber.plant(new Timber.DebugTree());

        // Ask for permissions
        if (!checkStrPermission(this, CALL_PHONE) || !checkStrPermission(this, SEND_SMS)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{CALL_PHONE, READ_CONTACTS, SEND_SMS}, 1);
        }

        // Prompt the user with a dialog to select this app to be the default phone app
        String packageName = getApplicationContext().getPackageName();

        if (!Objects.requireNonNull(getSystemService(TelecomManager.class)).getDefaultDialerPackage().equals(packageName)) {
            startActivity(new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName));
        }

        // Update contacts if possible
        if (checkStrPermission(this, READ_CONTACTS)) {
            updateContacts(false);
        }

        mDialerLayout = findViewById(R.id.dialer_layout);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        // Set this class as the listener for the fragments
        if (fragment instanceof DialFragment) {
            mDialFragment = (DialFragment) fragment;
            mDialFragment.setOnDialChangeListener(this);
        }
        if (fragment instanceof ContactsFragment) {
            mContactsFragment = (ContactsFragment) fragment;
            mContactsFragment.setOnContactsChangeListener(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isFirstInstance = PreferenceUtils.getInstance().getBoolean(R.string.pref_is_first_instance_key);

        // If this is the first time the user opens the app
        if (isFirstInstance) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                // If the user gave permission to look for contacts, look for 'em
                updateContacts(false);
                ContactsFragment contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.main_contacts_fragment);
                contactsFragment.populateListView();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.update_contacts:
                updateContacts(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNumberChanged(String number) {
        mContactsFragment.populateListView(number);
    }

    public void animateDialer(boolean trig) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (mDialFragment.isHidden() && trig) {
            mIsScrolling = false;
            ft.show(mDialFragment);
        } else if (mDialFragment.isVisible() && !trig) {
            mIsScrolling = true;
            ft.hide(mDialFragment);
        }
        ft.commit();

    }

    /**
     * Updates the contacts list in mContactsManager
     */
    private void updateContacts(boolean showProgress) {
        ContactsManager.updateContactsInBackground(this, showProgress);
    }

    // ===ContactsFragment Function=== //
    @Override
    public void onContactsScroll(boolean isScrolling) {
        animateDialer(!isScrolling);
    }

    @Override
    public void onContactsListItemClick(View view) {
        TextView textView = (TextView) view.findViewById(R.id.contact_list_number_text);
        String number = textView.getText().toString();
        mDialFragment.setNumber(number);
    }
}
