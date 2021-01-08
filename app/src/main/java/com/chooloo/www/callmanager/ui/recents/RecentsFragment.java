package com.chooloo.www.callmanager.ui.recents;

import android.Manifest;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader;
import com.chooloo.www.callmanager.entity.Contact;
import com.chooloo.www.callmanager.entity.RecentCall;
import com.chooloo.www.callmanager.ui.cursor.CursorFragment;
import com.chooloo.www.callmanager.util.ContactUtils;
import com.chooloo.www.callmanager.util.PermissionUtils;
import com.chooloo.www.callmanager.util.Utilities;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.WRITE_CALL_LOG;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class RecentsFragment extends CursorFragment<RecentsAdapter> implements RecentsMvpView {

    private static final String[] REQUIRED_PERMISSIONS = {READ_CALL_LOG, WRITE_CALL_LOG};
    private static final String ARG_CONTACT_NAME = "contact_name";
    private static final String ARG_PHONE_NUMBER = "phone_number";

    private RecentsPresenter<RecentsMvpView> mPresenter;

    public static RecentsFragment newInstance() {
        return RecentsFragment.newInstance(null, null);
    }

    public static RecentsFragment newInstance(@Nullable String phoneNumber, @Nullable String contactName) {
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactName);
        RecentsFragment fragment = new RecentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public RecentsAdapter onGetAdapter() {
        RecentsAdapter recentsAdapter = new RecentsAdapter(mActivity);
        recentsAdapter.setOnRecentItemClickListener(recentCall -> mPresenter.onRecentItemClick(recentCall));
        recentsAdapter.setOnRecentItemLongClickListener(recentCall -> mPresenter.onRecentItemLongClick(recentCall));
        return recentsAdapter;
    }

    @Override
    public String[] onGetPermissions() {
        return REQUIRED_PERMISSIONS;
    }

    @Override
    public Loader<Cursor> onGetLoader(Bundle args) {
        String contactName = args.getString(ARG_CONTACT_NAME, null);
        String phoneNumber = args.getString(ARG_PHONE_NUMBER, null);
        return new RecentsCursorLoader(mActivity, phoneNumber, contactName);
    }

    @Override
    public void onSetup() {
        super.onSetup();

        mLoaderId = 1;

        mPresenter = new RecentsPresenter<>();
        mPresenter.onAttach(this);

        load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDetach();
    }

    @Override
    public void load(@Nullable String phoneNumber, @Nullable String contactName) {
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        args.putString(ARG_CONTACT_NAME, contactName);
        setArguments(args);
        load();
    }

    @Override
    public void openRecent(RecentCall recentCall) {
        // TODO implement
    }
}
