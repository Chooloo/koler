package com.chooloo.www.callmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuItem;

import com.chooloo.www.callmanager.ContactsManager;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.MainPagerAdapter;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.Manifest.permission.READ_CONTACTS;

//TODO clean up, give this activity a purpose
public class MainActivity extends ToolbarActivity {

    @BindView(R.id.pager) ViewPager mViewPager;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;

    private MainPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceUtils.getInstance(this);
        ButterKnife.bind(this);

        // Init timber
        Timber.plant(new Timber.DebugTree());

        // Ask for permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, READ_CONTACTS}, 1);
        }

        // Prompt the user with a dialog to select this app to be the default phone app
        String packageName = getApplicationContext().getPackageName();

        if (!Objects.requireNonNull(getSystemService(TelecomManager.class)).getDefaultDialerPackage().equals(packageName)) {
            startActivity(new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName));
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            updateContacts(false);
        }

        mTabLayout.addTab(mTabLayout.newTab());

        //Instantiate ViewPager
        mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Updates the contacts list in mContactsManager
     */
    private void updateContacts(boolean showProgress) {
        ContactsManager.updateContactsInBackground(this, showProgress);
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

}
