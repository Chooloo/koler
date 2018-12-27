package com.chooloo.www.callmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;

import com.chooloo.www.callmanager.R;

public class MainActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Prompt the user with a dialog to select this app to be the default phone app
        String packageName = getApplicationContext().getPackageName();
        //noinspection ConstantConditions
        if (!getSystemService(TelecomManager.class).getDefaultDialerPackage().equals(packageName)) {
            startActivity(new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName));
        }
    }
}
