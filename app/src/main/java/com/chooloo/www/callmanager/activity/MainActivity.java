package com.chooloo.www.callmanager.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chooloo.www.callmanager.Contact;
import com.chooloo.www.callmanager.ContactsManager;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.PreferenceUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnLongClick;
import timber.log.Timber;

import static android.Manifest.permission.READ_CONTACTS;

//TODO clean up, give this activity a purpose
public class MainActivity extends ToolbarActivity {

    // Variables
    public static String sToNumber = "";

    // Local classes instances
    ContactsManager mContactsManager = new ContactsManager();
    ContactsAdapter mContactAdapter;

    @BindView(R.id.dial_contacts_list) ListView mContactsList;
    @BindView(R.id.text_number_input) EditText mNumberInput;
    @BindView(R.id.button_call) TextView mCallButton;
    @BindView(R.id.button_delete) TextView mDelButton;
    @BindView(R.id.table_numbers) TableLayout mNumbersTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceUtils.getInstance(this);
        ButterKnife.bind(this);

        // It's exactly what it sounds like
        makeBarTransparent();

        // Init timber
        Timber.plant(new Timber.DebugTree());

        // Ask for permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, READ_CONTACTS}, 1);
        }

        // Prompt the user with a dialog to select this app to be the default phone app
        String packageName = getApplicationContext().getPackageName();

        // noinspection ConstantConditions
        if (!getSystemService(TelecomManager.class).getDefaultDialerPackage().equals(packageName)) {
            startActivity(new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                    .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName));
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            updateContacts(false);
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
    protected void onDestroy() {
        super.onDestroy();
//        if (PreferenceUtils.getInstance().getBoolean(R.string.pref_is_first_instance_key)) {
//            PreferenceUtils.getInstance().putBoolean(R.string.pref_is_first_instance_key, false);
//        }
    }

    // -- Buttons -- //

    /**
     * If the user clicks an item from the list, set the mNumberInput text to
     * the one from the list item
     */
    @OnItemClick(R.id.dial_contacts_list)
    public void itemClicked(View view) {
        TextView listItem = (TextView) view.findViewById(R.id.contact_dial_list_number_text);
        mNumberInput.setText(listItem.getText().toString());
        Timber.i("Item clicked: %s", listItem.getText().toString());
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
        populateListViewTask populate = new populateListViewTask(sToNumber);
        populate.execute();
    }

    /**
     * Deletes a number from the keypad's input when the delete button is clicked
     */
    @OnClick(R.id.button_delete)
    public void delNum(View view) {
        if (sToNumber.length() <= 0) return;
        sToNumber = sToNumber.substring(0, sToNumber.length() - 1);
        mNumberInput.setText(sToNumber);
        populateListViewTask populate = new populateListViewTask(sToNumber);
        populate.execute();
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
     * When the mNumberInput is selected
     *
     * @param view which in this case is the mNumberInput
     */
    @OnClick(R.id.text_number_input)
    public void editTextSelected(View view) {
        hideKeyboard(mNumberInput);
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
                // Set the data for the call
                String uri = "tel:" + mNumberInput.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
                // Start the call
                startActivity(callIntent);
            } catch (SecurityException e) {
                Toast.makeText(this, "Couldn't call " + sToNumber, Toast.LENGTH_LONG).show();
                Timber.e(e, "Couldn't call %s", sToNumber);
            }
        }
    }

    public void makeBarTransparent() {
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.grey_back));
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
     * Updates the contacts list in mContactsManager
     */
    private void updateContacts(boolean showProgress) {
        ContactsManager.updateContactsInBackground(this, showProgress);
    }

    /**
     * Set the mContactAdapter to the list view
     *
     * @param contacts
     */
    private void populateListView(ArrayList<Contact> contacts) {
        mContactAdapter = new ContactsAdapter(contacts, this);
        mContactsList.setAdapter(mContactAdapter);
    }

    private class populateListViewTask extends AsyncTask<String, String, String> {

        ArrayList<Contact> mCurrentContacts = new ArrayList<Contact>();
        String mPhoneNumber;

        public populateListViewTask(String number) {
            this.mPhoneNumber = number;
        }

        @Override
        protected String doInBackground(String... strings) {
            publishProgress("Populating...");
            Timber.i("Looking for contacts to populate the listview");
            mCurrentContacts = ContactsManager.getContactsByNum(mPhoneNumber);
            return "1";
        }

        @Override
        protected void onPostExecute(String s) {
            populateListView(mCurrentContacts);
        }
    }

    private class ContactsAdapter extends ArrayAdapter<Contact> implements View.OnClickListener {

        private ArrayList<Contact> contacts;
        Context mContext;

        private class ViewHolder {
            TextView contactNameTxt;
            TextView contactNumTxt;
        }

        public ContactsAdapter(ArrayList<Contact> contacts, Context context) {
            super(context, R.layout.contact_dial_list_item, contacts);
            this.contacts = contacts;
            this.mContext = context;
        }

        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            Object object = getItem(position);
            Contact contact = (Contact) object;

            switch (v.getId()) {
                case R.id.contact_dial_list_name_text:
                    TextView nameText = (TextView) findViewById(R.id.contact_dial_list_number_text);
                    mNumberInput.setText(nameText.getText().toString());
                    break;
                case R.id.contact_dial_list_number_text:
                    TextView nameText2 = (TextView) findViewById(R.id.contact_dial_list_number_text);
                    mNumberInput.setText(nameText2.getText().toString());
                    break;
            }
        }

        private int lastPosition = -1;

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // Get the contact for this position
            Contact contact = getItem(position);
            ViewHolder viewHolder;

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                // Inflate
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.contact_dial_list_item, parent, false);

                // Get the item views
                viewHolder.contactNameTxt = (TextView) convertView.findViewById(R.id.contact_dial_list_name_text);
                viewHolder.contactNumTxt = (TextView) convertView.findViewById(R.id.contact_dial_list_number_text);

                // Final result
                result = convertView;
                convertView.setTag(viewHolder);

            } else {

                viewHolder = (ViewHolder) convertView.getTag();
                result = convertView;

            }

            // Set animation of added list item
            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;

            // Set the texts
            viewHolder.contactNameTxt.setText(contact.getName());
            viewHolder.contactNumTxt.setText(contact.getPhoneNumber());

            return convertView;
        }
    }
}
