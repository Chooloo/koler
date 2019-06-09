package com.chooloo.www.callmanager.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.SingleCGroupAdapter;
import com.chooloo.www.callmanager.adapter.helper.ItemTouchHelperListener;
import com.chooloo.www.callmanager.adapter.helper.SimpleItemTouchHelperCallback;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.util.CallManager;
import com.chooloo.www.callmanager.util.ThemeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CGroupActivity extends AbsAppBarActivity implements
        ItemTouchHelperListener {

    public static final String EXTRA_LIST_ID = "list_id";

    CGroupViewModel mViewModel;

    ItemTouchHelper mItemTouchHelper;
    SingleCGroupAdapter mAdapter;

    List<Contact> mContacts = null;

    ActionMode mActionMode = null;
    ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            mActionMode = mode;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_list_editing, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_dismiss:
                    mAdapter.enableEditMode(false);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            getWindow().setStatusBarColor(Color.WHITE);
        }
    };

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeType(ThemeUtils.TYPE_NORMAL);
        setContentView(R.layout.activity_cgroup);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_LIST_ID)) {
            throw new IllegalArgumentException("You must start this activity with a list id");
        }
        long listId = intent.getLongExtra(EXTRA_LIST_ID, -1);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mAdapter = new SingleCGroupAdapter(this, mRecyclerView, this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mViewModel = ViewModelProviders.of(this).get(CGroupViewModel.class);
        mViewModel.setListId(listId);
        mViewModel.getContacts().observe(this, contacts -> {
            mContacts = contacts;
            mAdapter.setData(contacts);
        });
        mViewModel.getCGroup().observe(this, cGroups -> {
            if (cGroups != null && !cGroups.isEmpty()) {
                setLabel(cGroups.get(0).getName());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // We want a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                startActionMode();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.fab_auto_call)
    public void autoCall(View view) {
        CallManager.startAutoCalling(mContacts, this, 0);
    }

    @Override
    public void onItemSelected(RecyclerView.ViewHolder holder) {
        startActionMode();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }

    @Override
    public void onStartSwipe(RecyclerView.ViewHolder holder) {
        mItemTouchHelper.startSwipe(holder);
    }

    /**
     * Activates action mode
     * I dont know wtf that means but thats what it does
     */
    private void startActionMode() {
        if (mActionMode != null) return;
        // Start the CAB using the ActionMode.Callback defined above
        mActionMode = startSupportActionMode(mActionModeCallback);

        mAdapter.enableEditMode(true);
        getWindow().setStatusBarColor(getColor(R.color.grey_100));
    }
}
