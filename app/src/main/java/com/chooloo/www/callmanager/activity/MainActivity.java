package com.chooloo.www.callmanager.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chooloo.www.callmanager.CallManager;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.PreferenceUtils;
import com.google.android.material.theme.MaterialComponentsViewInflater;

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
    @BindView(R.id.button_call) TextView mCallButton;
    @BindView(R.id.button_delete) TextView mDelButton;
    @BindView(R.id.table_numbers) TableLayout mNumbersTable;
    //-----------------
    @BindView(R.id.contactText) TextView mContactText;

    boolean isKeyboardDisabled = false;

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

        // Checks for permission to read contacts, is there is, updates the contacts list in CallManager
        if (ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            runAsyncTask(false);
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
                runAsyncTask(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When the mNumberInput is selected
     *
     * @param view which in this case is the mNumberInput
     */
    @OnClick(R.id.text_number_input)
    public void editTextSelected(View view) {
        hideKeyboard(mNumberInput);
    }

    /**
     * Hides the keyboard based on the focused view (most likely EditText)
     *
     * @param view is the focused view
     */
    public void hideKeyboard(EditText view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Dialer buttons click handler
     *
     * @param view is the button number
     */
    @OnClick({R.id.chip0, R.id.chip1, R.id.chip2, R.id.chip3, R.id.chip4, R.id.chip5, R.id.chip6, R.id.chip7, R.id.chip8, R.id.chip9, R.id.chip_star, R.id.chip_hex})
    public void addNum(View view) {
        String id = getResources().getResourceEntryName(view.getId());
        if (id.contains("chip_star")) sToNumber += "*";
        else if (id.contains("chip_hex")) sToNumber += "#";
        else {
            sToNumber += id.substring(4);
        }
        mNumberInput.setText(sToNumber);
    }

    /**
     * Deletes a number from the keypad's input when the delete button is clicked
     */
    @OnClick(R.id.button_delete)
    public void delNum(View view) {
        if (sToNumber.length() <= 0) return;
        sToNumber = sToNumber.substring(0, sToNumber.length() - 1);
        mNumberInput.setText(sToNumber);
    }

    /**
     * Deletes the whole keypad's input when the delete button is long clicked
     */
    @OnLongClick(R.id.button_delete)
    public boolean delAllNum(View view) {
        sToNumber = "";
        mNumberInput.setText(sToNumber);
        return true;
    }

    /**
     * Starts a call to voice mail when the 1 button is long clicked
     */
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

    /**
     * Calls the number in the keypad's input
     */
    @OnClick(R.id.button_call)
    public void call(View view) {
        Timber.i("Trying to call: " + mNumberInput.getText().toString());
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

    /**
     * Updates the contacts list in CallManager
     */
    private void updateContacts() {
        CallManager.sContacts = CallManager.getContactList(this);
    }

    /**
     * Creates an instant of AsyncTaskRunner and executes it
     */
    private void runAsyncTask(boolean showProgress) {
        AsyncTaskRunner runner = new AsyncTaskRunner(showProgress);
        runner.execute();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private boolean showProgress;
        private String status;
        ProgressDialog progressDialog;

        public AsyncTaskRunner(boolean showProgress) {
            this.showProgress = showProgress;
        }

        @Override
        protected String doInBackground(String... objects) {
            publishProgress("Getting Contacts...");
            try {
                updateContacts();
            } catch (Exception e) {
                Timber.e(e);
                status = "Something went wrong, try again later";
            }
            return status;
        }

        @Override
        protected void onPreExecute() {
            if (showProgress) {
                progressDialog = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
                progressDialog.setTitle("Updating Contacts");
                progressDialog.setMessage("I bet you can't even count to 10");
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (showProgress) progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Updated contacts successfuly", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }
    }

}
