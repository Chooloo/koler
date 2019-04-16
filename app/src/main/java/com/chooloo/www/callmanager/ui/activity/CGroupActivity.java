package com.chooloo.www.callmanager.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.CGroupDetailsAdapter;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CGroupActivity extends AbsAppBarActivity {

    public static final String EXTRA_LIST_ID = "list_id";

    CGroupViewModel mViewModel;
    CGroupDetailsAdapter mAdapter;

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
        mViewModel.getContacts().observe(this, contacts -> mAdapter.setData(contacts));
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
}
