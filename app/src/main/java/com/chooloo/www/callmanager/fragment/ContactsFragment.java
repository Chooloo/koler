package com.chooloo.www.callmanager.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chooloo.www.callmanager.activity.MainActivity;
import com.chooloo.www.callmanager.database.Contact;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.ContactsManager;
import com.chooloo.www.callmanager.util.Utilities;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.Manifest.permission.READ_CONTACTS;
import static com.chooloo.www.callmanager.util.Utilities.checkStrPermission;


public class ContactsFragment extends Fragment implements AdapterView.OnItemClickListener {

    OnContactsChangeListener mCallback;

    ArrayList<Contact> mCurrentContacts;

    // Local classes instances
    private ContactsAdapter mContactAdapter;

    // Views
    ViewGroup mRootView;
    @BindView(R.id.list_contacts) ListView mContactsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (checkStrPermission(getContext(), READ_CONTACTS)) {
            populateListView();
        }
        mContactsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) mCallback.onContactsScroll(false);
                else mCallback.onContactsScroll(true);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mContactsList.setOnItemClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Arrays.asList(permissions).contains(READ_CONTACTS)) {
            populateListView();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textv = view.findViewById(R.id.contact_list_number_text);
        Timber.i("item clicked with number: " + textv.getText().toString());
        mCallback.onContactsListItemClick(view);
    }

    // ===mCallback=== //

    /**
     * Set the given activity as the listener
     *
     * @param activity
     */
    public void setOnContactsChangeListener(OnContactsChangeListener activity) {
        mCallback = activity;
    }

    public interface OnContactsChangeListener {

        public void onContactsScroll(boolean isScrolling);

        public void onContactsListItemClick(View view);

    }

    // ===Populate ListView=== //

    /**
     * Creates a new populateListViewTask and executes it without a given number
     * Basically populates the list view
     */
    public void populateListView() {
        populateListViewTask populateList = new populateListViewTask();
        populateList.execute();
    }

    /**
     * Creates a new populateListViewTask and executes it with a given number
     * Basically populates the list view by a number
     */
    public void populateListView(String number) {
        populateListViewTask populateList = new populateListViewTask(number);
        populateList.execute();
    }

    /**
     * Set the mContactAdapter to the list view
     *
     * @param contacts
     */
    private void populateListViewArray(ArrayList<Contact> contacts) {
        mContactAdapter = new ContactsAdapter(contacts, getContext());
        mContactsList.setAdapter(mContactAdapter);
    }

    /**
     * If a number is given, returns all the contacts with the given number
     * If no number is given, returns all the contacts
     */
    private class populateListViewTask extends AsyncTask<String, String, String> {

        ArrayList<Contact> mContacts = new ArrayList<Contact>();
        String mPhoneNumber;

        /**
         * Constructor
         *
         * @param number get all the contacts that has that number
         */
        public populateListViewTask(String number) {
            this.mPhoneNumber = number;
        }

        /**
         * Constructor that takes no parameters
         * Meaning getting all the contacts
         */
        public populateListViewTask() {
            this.mPhoneNumber = null;
        }

        @Override
        protected String doInBackground(String... strings) {
            publishProgress("Populating...");
            Timber.i("Looking for contacts to populate the listview");
            if (mPhoneNumber == null)
                mContacts = ContactsManager.getContactList(getContext());
            else mContacts = ContactsManager.getContactsByNum(mPhoneNumber);
            return "1";
        }

        @Override
        protected void onPostExecute(String s) {
            mCurrentContacts = mContacts;
            populateListViewArray(mContacts);
        }
    }

    /**
     * Populates the list view with contacts
     */
    private class ContactsAdapter extends ArrayAdapter<Contact> implements View.OnClickListener {

        private ArrayList<Contact> contacts;
        Context mContext;

        /**
         * A class for a view holder which is the items populating the list
         */
        private class ViewHolder {
            TextView contactNameTxt;
            TextView contactNumTxt;
            ImageView contactImagePlaceholder;
            ImageView contactImage;
        }

        /**
         * Constructor, takes a list of contacts and the context
         *
         * @param contacts a list of contacts
         * @param context
         */
        public ContactsAdapter(ArrayList<Contact> contacts, Context context) {
            super(context, R.layout.contact_list_item, contacts);
            this.contacts = contacts;
            this.mContext = context;
        }

        // Not in use right now but keep it in case we want to use it
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            Object object = getItem(position);
            Contact contact = (Contact) object;

            switch (v.getId()) {
                case R.id.contact_list_name_text:
                    break;
                case R.id.contact_list_number_text:
                    break;
            }
        }

        private int lastPosition = -1;

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            // Get the contact for this position
            Contact contact = getItem(position);
            ContactsAdapter.ViewHolder viewHolder;

            final View result;

            if (convertView == null) {

                viewHolder = new ContactsAdapter.ViewHolder();

                // Inflate
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.contact_list_item, parent, false);

                // Get the item views
                viewHolder.contactNameTxt = convertView.findViewById(R.id.contact_list_name_text);
                viewHolder.contactNumTxt = convertView.findViewById(R.id.contact_list_number_text);
                viewHolder.contactImage = convertView.findViewById(R.id.list_image_photo);
                viewHolder.contactImagePlaceholder = convertView.findViewById(R.id.list_image_placeholder);

                // Final result
                result = convertView;
                convertView.setTag(viewHolder);

            } else {

                viewHolder = (ContactsAdapter.ViewHolder) convertView.getTag();
                result = convertView;

            }

            // Set animation of added list item
            Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            result.startAnimation(animation);
            lastPosition = position;

            // Set the texts
            viewHolder.contactNameTxt.setText(contact.getName());
            viewHolder.contactNumTxt.setText(Utilities.formatPhoneNumber(contact.getMainPhoneNumber()));

            //  Set the image
            if (contact.getPhotoUri() != null && !contact.getName().isEmpty()) {
                viewHolder.contactImagePlaceholder.setVisibility(View.INVISIBLE);
                viewHolder.contactImage.setVisibility(View.VISIBLE);
                viewHolder.contactImage.setImageURI(Uri.parse(contact.getPhotoUri()));
            }


            return convertView;
        }
    }
}
