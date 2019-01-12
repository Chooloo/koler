package com.chooloo.www.callmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chooloo.www.callmanager.CallManager;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.Stopwatch;
import com.chooloo.www.callmanager.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import timber.log.Timber;

import static android.Manifest.permission.READ_CONTACTS;

//TODO clean up, give this activity a purpose
public class MainActivity extends ToolbarActivity {

    public static String sToNumber = "";

    @BindView(R.id.text_number_input) EditText mNumberInput;
    @BindView(R.id.button_call) Button mCallButton;
    @BindView(R.id.table_numbers) TableLayout mNumbersTable;
    //-----------------
    @BindView(R.id.contactText) TextView mContactText;


    Handler contactSearchHandler = new Handler(){

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceUtils.getInstance(this);
        ButterKnife.bind(this);

        //Init timber
        Timber.plant(new Timber.DebugTree());

        // Ask for permissions
        // CALL_PHONE, READ_CONTACTS
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, READ_CONTACTS}, 1);
        }

        //Prompt the user with a dialog to select this app to be the default phone app
        String packageName = getApplicationContext().getPackageName();
        //noinspection ConstantConditions
        if (!getSystemService(TelecomManager.class).getDefaultDialerPackage().equals(packageName)) {
            startActivity(new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        getSupportActionBar().setElevation(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.chip0, R.id.chip1, R.id.chip2, R.id.chip3, R.id.chip4, R.id.chip5, R.id.chip6, R.id.chip7, R.id.chip8, R.id.chip9, R.id.chip_star, R.id.chip_hex})
    public void addNum(View view) {
        sToNumber += ((Button) view).getText();
//        TODO finish and fix the shit below (contact searcher) do this with the handler above
//        if (sToNumber.length() > 5) {
//            Map<String, String> matchedContacts = CallManager.getContactsByNum(this, sToNumber);
//            if (matchedContacts.size() == 1) {
//                for (Map.Entry<String, String> contact : matchedContacts.entrySet()) {
//                    sToNumber = contact.getValue();
//                }
//            }
//            for (Map.Entry<String, String> contact : matchedContacts.entrySet()) {
//                mContactText.setText(mContactText.getText() + " " + contact.getKey());
//            }

//                for (int i = 0; i < matchedContacts.size(); i++) {
//                    mContactText.setText(mContactText.getText() + " " + matchedContacts.get(i));
//                }

//        }
        mNumberInput.setText(sToNumber);
    }

    @OnClick(R.id.button_delete)
    public void delNum(View view) {
        if (sToNumber.length() <= 0) return;
        sToNumber = sToNumber.substring(0, sToNumber.length() - 1);
        mNumberInput.setText(sToNumber);
    }

    @OnLongClick(R.id.button_delete)
    public boolean delAllNum(View view) {
        sToNumber = "";
        mNumberInput.setText(sToNumber);
        return true;
    }

    @OnLongClick(R.id.chip1)
    public boolean startVoiceMail(View view) {
        try {
            Uri uri = Uri.parse("voicemail:1");
            Intent voiceMail = new Intent(Intent.ACTION_CALL, uri);
            startActivity(voiceMail);
            return true;
        } catch (SecurityException e) {
            Toast.makeText(this, "Couldn't start Voicemail", Toast.LENGTH_LONG).show();
            Timber.e(e);
            return false;
        }
    }

    @OnClick(R.id.button_call)
    public void call(View view) {
        if (mNumberInput.getText() == null) {
            Toast.makeText(this, "Calling without a number huh? U little shit", Toast.LENGTH_LONG).show();
        } else {
            try {
                // Set the data
                String uri = "tel:" + mNumberInput.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

                startActivity(callIntent);
            } catch (SecurityException e) {
                Toast.makeText(this, "Couldn't call " + sToNumber, Toast.LENGTH_LONG).show();
                Timber.e(e, "Couldn't call %s", sToNumber);
            }
        }
    }
}
