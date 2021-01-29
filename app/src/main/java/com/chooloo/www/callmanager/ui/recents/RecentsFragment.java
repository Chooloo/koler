package com.chooloo.www.callmanager.ui.recents;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.loader.content.Loader;

import com.chooloo.www.callmanager.cursorloader.RecentsCursorLoader;
import com.chooloo.www.callmanager.entity.RecentCall;
import com.chooloo.www.callmanager.ui.cursor.CursorFragment;

import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.WRITE_CALL_LOG;

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
        RecentsAdapter recentsAdapter = new RecentsAdapter(activity);
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
        return new RecentsCursorLoader(activity, phoneNumber, contactName);
    }

    @Override
    public void onSetup() {
        super.onSetup();

        mLoaderId = 1;

        mPresenter = new RecentsPresenter<>();
        mPresenter.attach(this);

        load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detach();
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
