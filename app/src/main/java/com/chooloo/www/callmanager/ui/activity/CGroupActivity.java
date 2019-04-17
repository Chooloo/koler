package com.chooloo.www.callmanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.CGroupDetailsAdapter;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.util.CallManager;

import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CGroupActivity extends AbsAppBarActivity {

    public static final String EXTRA_LIST_ID = "list_id";

    CGroupViewModel mViewModel;
    CGroupDetailsAdapter mAdapter;
    List<Contact> mContacts = null;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgroup);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_LIST_ID)) {
            throw new IllegalArgumentException("You must start this activity with a list id");
        }
        long listId = intent.getLongExtra(EXTRA_LIST_ID, -1);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mAdapter = new CGroupDetailsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

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

        //We want a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.fab_auto_call)
    public void autoCall(View view) {
        CallManager.startAutoCalling(mContacts, this, 0);
    }
}
