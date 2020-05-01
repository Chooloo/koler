package com.chooloo.www.callmanager.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.WrapperListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.RecentsAdapter;
import com.chooloo.www.callmanager.adapter.listener.OnItemClickListener;
import com.chooloo.www.callmanager.adapter.listener.OnItemLongClickListener;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.database.entity.RecentCall;
import com.chooloo.www.callmanager.google.FastScroller;
import com.chooloo.www.callmanager.google.RecentsCursorLoader;
import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.activity.MainActivity;
import com.chooloo.www.callmanager.ui.fragment.base.AbsRecyclerViewFragment;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.viewmodels.SharedDialViewModel;
import com.chooloo.www.callmanager.viewmodels.SharedSearchViewModel;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import timber.log.Timber;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.View.GONE;
import static com.chooloo.www.callmanager.util.Utilities.PERMISSION_RC;

public class RecentsFragment extends AbsRecyclerViewFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        FABCoordinator.FABDrawableCoordination,
        FABCoordinator.OnFABClickListener,
        View.OnScrollChangeListener, OnItemClickListener, OnItemLongClickListener {

    private static final int LOADER_ID = 1;
    private static final String ARG_PHONE_NUMBER = "phone_number";
    private static final String ARG_CONTACT_NAME = "contact_name";

    LinearLayoutManager mLayoutManager;
    RecentsAdapter mRecentsAdapter;

    // View Models
    private SharedDialViewModel mSharedDialViewModel;
    private SharedSearchViewModel mSharedSearchViewModel;

    // ViewBinds
    @BindView(R.id.recents_refresh_layout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.fast_scroller) FastScroller mFastScroller;
    @BindView(R.id.enable_recents_btn) Button mEnableCallLogButton;
    @BindView(R.id.empty_state) View mEmptyState;
    @BindView(R.id.empty_title) TextView mEmptyTitle;
    @BindView(R.id.empty_desc) TextView mEmptyDesc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recents, container, false);
    }

    // -- Overrides -- //

    @Override
    protected void onFragmentReady() {

        checkShowButton();

        mLayoutManager =
                new LinearLayoutManager(getContext()) {
                    @Override
                    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                        super.onLayoutChildren(recycler, state);
                        int itemsShown = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1;
                        if (mRecentsAdapter.getItemCount() > itemsShown) {
                            mFastScroller.setVisibility(View.VISIBLE);
                            mRecyclerView.setOnScrollChangeListener(RecentsFragment.this);
                        } else {
                            mFastScroller.setVisibility(GONE);
                        }
                    }
                };
        mRecentsAdapter = new RecentsAdapter(getContext(), null, this, this);

        // mRecyclerView
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mRecentsAdapter);

        // mRefreshLayout
        mRefreshLayout.setOnRefreshListener(() -> {
            LoaderManager.getInstance(RecentsFragment.this).restartLoader(LOADER_ID, null, RecentsFragment.this);
            tryRunningLoader();
        });

        mEmptyTitle.setText(R.string.empty_recents_title);
        mEmptyDesc.setText(R.string.empty_recents_desc);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Dialer View Model
        mSharedDialViewModel = ViewModelProviders.of(getActivity()).get(SharedDialViewModel.class);
        mSharedDialViewModel.getNumber().observe(this, s -> {
            if (isLoaderRunning()) {
                Bundle args = new Bundle();
                args.putString(ARG_PHONE_NUMBER, s);
                LoaderManager.getInstance(RecentsFragment.this).restartLoader(LOADER_ID, args, RecentsFragment.this);
            }
        });

        // Search Bar View Model
        mSharedSearchViewModel = ViewModelProviders.of(getActivity()).get(SharedSearchViewModel.class);
        mSharedSearchViewModel.getText().observe(this, t -> {
            if (isLoaderRunning()) {
                Bundle args = new Bundle();
                args.putString(ARG_CONTACT_NAME, t);
                LoaderManager.getInstance(RecentsFragment.this).restartLoader(LOADER_ID, args, RecentsFragment.this);
            }
        });

        tryRunningLoader();
    }

    @Override
    public void onResume() {
        super.onResume();
        tryRunningLoader();
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mFastScroller.updateContainerAndScrollBarPosition(mRecyclerView);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, Object data) {
        RecentCall recentCall = (RecentCall) data;
        showRecentPopup(recentCall);
//        CallManager.call(this.getContext(), recentCall.getCallerNumber());
    }

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder holder, Object data) {
//        RecentCall recentCall = (RecentCall) data;
//        showRecentPopup(recentCall);
    }


    // -- Loader -- //

    private void tryRunningLoader() {
        if (!isLoaderRunning() && Utilities.checkPermissionsGranted(getContext(), READ_CALL_LOG)) {
            runLoader();
            mEnableCallLogButton.setVisibility(GONE);
        }
    }

    private void runLoader() {
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    private boolean isLoaderRunning() {
        Loader loader = LoaderManager.getInstance(this).getLoader(LOADER_ID);
        return loader != null;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String contactName = null;
        String phoneNumber = null;
        if (args != null && args.containsKey(ARG_PHONE_NUMBER)) {
            phoneNumber = args.getString(ARG_PHONE_NUMBER);
        }
        if (args != null && args.containsKey(ARG_CONTACT_NAME)) {
            contactName = args.getString(ARG_CONTACT_NAME);
        }

        RecentsCursorLoader recentsCursorLoader = new RecentsCursorLoader(getContext(), phoneNumber, contactName);
        return recentsCursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        setData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mRecentsAdapter.changeCursor(null);
    }

    private void setData(Cursor data) {
        mRecentsAdapter.changeCursor(data);
        mFastScroller.setup(mRecentsAdapter, mLayoutManager);
        if (mRefreshLayout.isRefreshing()) mRefreshLayout.setRefreshing(false);
        if (data != null && data.getCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyState.setVisibility(GONE);
        } else {
            mRecyclerView.setVisibility(GONE);
            mEmptyState.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checking whither to show the "enable contacts" button
     */
    public void checkShowButton() {
        if (Utilities.checkPermissionsGranted(getContext(), READ_CALL_LOG)) {
            mEnableCallLogButton.setVisibility(GONE);
        } else {
            mEnableCallLogButton.setVisibility(View.VISIBLE);
        }
    }

    // -- On Clicks -- //

    @OnClick(R.id.enable_recents_btn)
    public void askForCallLogPermission() {
        Utilities.askForPermission(getActivity(), READ_CALL_LOG);
    }

    // -- FABCoordinator.OnFabClickListener -- //

    @Override
    public void onRightClick() {
        ((MainActivity) getActivity()).expandDialer(true);
    }

    @Override
    public void onLeftClick() {
        ((MainActivity) getActivity()).toggleSearchBar();
    }

    // -- FABCoordinator.FABDrawableCoordinator -- //

    @Override
    public int[] getIconsResources() {
        return new int[]{
                R.drawable.ic_dialpad_black_24dp,
                R.drawable.ic_search_black_24dp
        };
    }

    // -- Other -- //

    /**
     * Shows a pop up window (dialog) with the contact's information
     *
     * @param recentCall
     */
    private void showRecentPopup(RecentCall recentCall) {

        Contact contact;
//        if (recentCall.getCallerNumber() != null) {
//            Timber.i("Caller name: %s ", recentCall.getCallerName());
//            if (recentCall.getCallerName() != null)
//                contact = ContactUtils.getContactByName(getContext(), recentCall.getCallerName());
//            else
//                contact = ContactUtils.getContactByPhoneNumber(getContext(), recentCall.getCallerNumber());
//        } else {
//            contact = null;
//        }

        if (recentCall.getCallerName() != null)
            contact = ContactUtils.getContactByName(getContext(), recentCall.getCallerName());
        else contact = new Contact(null, recentCall.getCallerNumber(), null);

        // Initiate the dialog
        Dialog contactDialog = new Dialog(getContext());
        contactDialog.setContentView(R.layout.contact_popup_view);

        // Views declarations
        ConstraintLayout popupLayout;
        TextView contactName, contactNumber, contactDate;
        ImageView contactPhoto, contactPhotoPlaceholder;
        ImageButton callButton, editButton, deleteButton, infoButton, addButton, smsButton, favButton;

        popupLayout = contactDialog.findViewById(R.id.contact_popup_layout);

        contactPhoto = contactDialog.findViewById(R.id.contact_popup_photo);
        contactPhotoPlaceholder = contactDialog.findViewById(R.id.contact_popup_photo_placeholder);

        contactName = contactDialog.findViewById(R.id.contact_popup_name);
        contactNumber = contactDialog.findViewById(R.id.contact_popup_number);
        contactDate = contactDialog.findViewById(R.id.contact_popup_date);

        callButton = contactDialog.findViewById(R.id.contact_popup_button_call);
        editButton = contactDialog.findViewById(R.id.contact_popup_button_edit);
        deleteButton = contactDialog.findViewById(R.id.contact_popup_button_delete);
        infoButton = contactDialog.findViewById(R.id.contact_popup_button_info);
        addButton = contactDialog.findViewById(R.id.contact_popup_button_add);
        smsButton = contactDialog.findViewById(R.id.contact_popup_button_sms);

        contactDialog.findViewById(R.id.contact_popup_button_fav).setVisibility(GONE);

        if (contact.getName() != null) {
            contactName.setText(contact.getName());
            contactNumber.setText(Utilities.formatPhoneNumber(contact.getMainPhoneNumber()));
            infoButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            if (contact.getPhotoUri() == null || contact.getPhotoUri().isEmpty()) {
                contactPhoto.setVisibility(GONE);
                contactPhotoPlaceholder.setVisibility(View.VISIBLE);
            } else {
                contactPhoto.setVisibility(View.VISIBLE);
                contactPhotoPlaceholder.setVisibility(GONE);
                contactPhoto.setImageURI(Uri.parse(contact.getPhotoUri()));
            }
        } else {
            infoButton.setVisibility(GONE);
            editButton.setVisibility(GONE);
            addButton.setVisibility(View.VISIBLE);
            contactName.setText(Utilities.formatPhoneNumber(contact.getMainPhoneNumber()));
            contactNumber.setVisibility(GONE);

            contactPhoto.setVisibility(GONE);
            contactPhotoPlaceholder.setVisibility(View.VISIBLE);
        }

        contactDate.setVisibility(View.VISIBLE);
        contactDate.setText(recentCall.getCallDateString());

        // -- Click Listeners -- //
        if (contact.getName() != null) {

            editButton.setOnClickListener(v -> {
                ContactUtils.openContactToEditById(getActivity(), contact.getContactId());
            });

            infoButton.setOnClickListener(v -> {
                ContactUtils.openContactById(getActivity(), contact.getContactId());
            });

        }

        addButton.setOnClickListener(v -> {
            ContactUtils.addContactIntent(getActivity(), recentCall.getCallerNumber());
        });

        smsButton.setOnClickListener(v -> {
            Utilities.openSmsWithNumber(getActivity(), recentCall.getCallerNumber());
        });

        callButton.setOnClickListener(v -> {
            CallManager.call(this.getContext(), contact.getMainPhoneNumber());
        });

        deleteButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALL_LOG) == PERMISSION_GRANTED) {
                String queryString = "_ID=" + recentCall.getCallId();
                getActivity().getContentResolver().delete(CallLog.Calls.CONTENT_URI, queryString, null);
                tryRunningLoader();
                Toast.makeText(getContext(), "Call log deleted", Toast.LENGTH_SHORT).show();
                contactDialog.dismiss();
            } else {
                Toast.makeText(getContext(), "I dont have the permission", Toast.LENGTH_LONG).show();
                Utilities.askForPermission(getActivity(), WRITE_CALL_LOG);
                contactDialog.dismiss();
            }
        });

        popupLayout.setElevation(20);

        contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        contactDialog.show();

    }
}

