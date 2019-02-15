package com.chooloo.www.callmanager.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.fragment.ContactsFragment;
import com.chooloo.www.callmanager.fragment.DialFragment;
import com.chooloo.www.callmanager.util.ContactsManager;
import com.chooloo.www.callmanager.util.PreferenceUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;
import static com.chooloo.www.callmanager.util.Utilities.askForPermissions;
import static com.chooloo.www.callmanager.util.Utilities.checkPermissionGranted;

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
        if (!checkPermissionGranted(this, CALL_PHONE) || !checkPermissionGranted(this, SEND_SMS)) {
            askForPermissions(this, new String[]{CALL_PHONE, READ_CONTACTS, SEND_SMS});
        }

        // Prompt the user with a dialog to select this app to be the default phone app
        String packageName = getApplicationContext().getPackageName();

        if (!Objects.requireNonNull(getSystemService(TelecomManager.class)).getDefaultDialerPackage().equals(packageName)) {
            startActivity(new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName));
        }

        // Update contacts if possible
        if (checkPermissionGranted(this, READ_CONTACTS)) {
            updateContacts(false);
        }

        mDialerLayout = findViewById(R.id.dialer_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO this is not good - need to consider ViewModel
        NavHostFragment navFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        mContactsFragment = (ContactsFragment) navFragment.getChildFragmentManager().getFragments().get(0);
        mContactsFragment.setOnContactsChangeListener(this);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        // Set this class as the listener for the fragments
        if (fragment instanceof DialFragment) {
            mDialFragment = (DialFragment) fragment;
            mDialFragment.setOnDialChangeListener(this);
        }
    }

    /**
     * Triggered when the user gives some kind of a permission
     * (Usually through a permission dialog)
     *
     * @param requestCode
     * @param permissions  the permissions given
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isFirstInstance = PreferenceUtils.getInstance().getBoolean(R.string.pref_is_first_instance_key);

        // If this is the first time the user opens the app
        if (isFirstInstance) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                // If the user gave permission to look for contacts, look for 'em
                updateContacts(false);
            }

            PreferenceUtils.getInstance().putBoolean(R.string.pref_is_first_instance_key, true);
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

    /**
     * A function implemented from the dialer fragment (a callback method)
     *
     * @param number
     */
    @Override
    public void onNumberChanged(String number) {
        mContactsFragment.populateListView(number);
    }

    // -- On Clicks -- //

    @OnClick(R.id.button2)
    public void switchFragments(View view) {
        NavController controller = Navigation.findNavController(this, R.id.main_fragment);
        int id = controller.getCurrentDestination().getId();
        if (id == R.id.contactsFragment) {
            controller.navigate(R.id.action_contactsFragment_to_customContactsFragment);
        } else {
            controller.navigate(R.id.action_customContactsFragment_to_contactsFragment);
        }
    }

    // -- Other -- //

    /**
     * Hides or Shows the dialer according to the given parameter
     * If it needs to show the dialer it waits for 700 milis and shows
     * (In case the user keeps scrolling after he stopped)
     *
     * @param trig
     */
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

    // -- ContactsFragment Function -- //

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
