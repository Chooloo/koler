package com.chooloo.www.callmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.chooloo.www.callmanager.R;
import com.google.android.material.chip.Chip;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

//TODO clean up, give this activity a purpose
public class MainActivity extends ToolbarActivity implements View.OnClickListener {

    public static String sToNumber = "";
    @BindView(R.id.text_number_input)
    EditText mNumberInput;
    @BindView(R.id.button_call)
    Button mCallButton;
    @BindView(R.id.numbers_table)
    TableLayout mNumbersTable;
    @BindView(R.id.chip0)
    Chip mChip0;
    @BindView(R.id.chip1)
    Chip mChip1;
    @BindView(R.id.chip2)
    Chip mChip2;
    @BindView(R.id.chip3)
    Chip mChip3;
    @BindView(R.id.chip4)
    Chip mChip4;
    @BindView(R.id.chip5)
    Chip mChip5;
    @BindView(R.id.chip6)
    Chip mChip6;
    @BindView(R.id.chip7)
    Chip mChip7;
    @BindView(R.id.chip8)
    Chip mChip8;
    @BindView(R.id.chip9)
    Chip mChip9;
    @BindView(R.id.chipDel)
    Chip mChipDel;
    @BindView(R.id.chipStar)
    Chip mChipStar;
    @BindView(R.id.chipSulam)
    Chip mChipSulam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Init timber
        Timber.plant(new Timber.DebugTree());

        // Set click listeners for all the number chips
        mChip0.setOnClickListener(this);
        mChip1.setOnClickListener(this);
        mChip2.setOnClickListener(this);
        mChip3.setOnClickListener(this);
        mChip4.setOnClickListener(this);
        mChip5.setOnClickListener(this);
        mChip6.setOnClickListener(this);
        mChip7.setOnClickListener(this);
        mChip8.setOnClickListener(this);
        mChip9.setOnClickListener(this);
        mChipStar.setOnClickListener(this);
        mChipSulam.setOnClickListener(this);
        mChipDel.setOnClickListener(this);

        // Ask for permissions
        // READ_PHONE_STATE
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        // CALL_PHONE
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

        //Prompt the user with a dialog to select this app to be the default phone app
        String packageName = getApplicationContext().getPackageName();
        //noinspection ConstantConditions
        if (!getSystemService(TelecomManager.class).getDefaultDialerPackage().equals(packageName)) {
            startActivity(new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName));
        }
    }

    //TODO shorten this code and make it responsive
    @Override
    public void onClick(View v) {
        String id = v.getResources().getResourceName(v.getId());
        if (id.contains("chipDel")) {
            if (sToNumber.length() > 0) {
                sToNumber = sToNumber.substring(0, sToNumber.length() - 1);
            }
        } else if (id.contains("chip")) {
            v = (Chip) v;
            sToNumber += ((Chip) v).getText();
        }
//        switch (v.getId()) {
//            case R.id.chip0:
//                sToNumber += "0";
//                break;
//            case R.id.chip1:
//                sToNumber += "1";
//                break;
//            case R.id.chip2:
//                sToNumber += "2";
//                break;
//            case R.id.chip3:
//                sToNumber += "3";
//                break;
//            case R.id.chip4:
//                sToNumber += "4";
//                break;
//            case R.id.chip5:
//                sToNumber += "5";
//                break;
//            case R.id.chip6:
//                sToNumber += "6";
//                break;
//            case R.id.chip7:
//                sToNumber += "7";
//                break;
//            case R.id.chip8:
//                sToNumber += "8";
//                break;
//            case R.id.chip9:
//                sToNumber += "9";
//                break;
//            case R.id.chipStar:
//                sToNumber += "*";
//                break;
//            case R.id.chipSulam:
//                sToNumber += "#";
//                break;
//            case R.id.chipDel:
//                if (sToNumber.length() > 0) {
//                    sToNumber = sToNumber.substring(0, sToNumber.length() - 1);
//                }
//                break;
//        }
        mNumberInput.setText(sToNumber);
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
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_call)
    public void call(View view) {
        if (mNumberInput.getText() == null) {
            Toast.makeText(getApplicationContext(), "Calling without a number huh? U little shit", Toast.LENGTH_LONG).show();
        } else {
            try {
                // Set the data
                String uri = "tel:" + mNumberInput.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

                startActivity(callIntent);
            } catch (SecurityException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong... Fuck.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}
