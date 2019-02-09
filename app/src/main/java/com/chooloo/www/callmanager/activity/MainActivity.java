package com.chooloo.www.callmanager.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chooloo.www.callmanager.OnSwipeTouchListener;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.fragment.ContactsFragment;
import com.chooloo.www.callmanager.fragment.DialFragment;
import com.chooloo.www.callmanager.util.ContactsManager;
import com.chooloo.www.callmanager.util.PreferenceUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.SEND_SMS;
import static com.chooloo.www.callmanager.util.Utilities.checkStrPermission;

public class MainActivity extends AppBarActivity {

    @BindView(R.id.activity_main) ConstraintLayout mMainLayout;

    ViewGroup mDialerLayout;
    DialFragment mDialFragment;

    OnSwipeTouchListener mContactsSwipeListener;

    @BindView(R.id.button) Button button;

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
        mDialFragment = (DialFragment) getSupportFragmentManager().findFragmentById(R.id.main_dialer_fragment);

        mContactsSwipeListener = new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeTop() {
                animateDialer(mMainLayout);
            }
        };


    }

    @OnClick(R.id.button)
    public void animateDialer(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (mDialFragment.isHidden()) {
            ft.show(mDialFragment);
        } else {
            ft.hide(mDialFragment);
        }
        ft.commit();
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isFirstInstance = PreferenceUtils.getInstance().getBoolean(R.string.pref_is_first_instance_key);

        // If this is the first time the user opens the app
        if (isFirstInstance) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                // If the user gave permission to look for contacts, look for 'em
                updateContacts(false);
                ContactsFragment contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
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

    /**
     * Updates the contacts list in mContactsManager
     */
    private void updateContacts(boolean showProgress) {
        ContactsManager.updateContactsInBackground(this, showProgress);
    }

}
