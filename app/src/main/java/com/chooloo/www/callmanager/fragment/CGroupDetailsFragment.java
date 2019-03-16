package com.chooloo.www.callmanager.fragment;

import android.os.Bundle;
import android.view.View;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.adapter.CGroupDetailsAdapter;
import com.chooloo.www.callmanager.fragment.base.AbsRecyclerViewFragment;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.OnClick;

public class CGroupDetailsFragment extends AbsRecyclerViewFragment {

    private CGroupDetailsViewModel mViewModel;

    private CGroupDetailsAdapter mAdapter;

    @Override
    protected void onCreateView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAdapter = new CGroupDetailsAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_cgroup_details;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args == null || !args.containsKey(getString(R.string.arg_list_id))) {
            throw new IllegalArgumentException("Must have a list id argument passed to the fragment");
        }

        long listId = args.getLong(getString(R.string.arg_list_id));
        mViewModel = ViewModelProviders.of(this).get(CGroupDetailsViewModel.class);
        mViewModel.setListId(listId);
        mViewModel.getContacts().observe(this, contacts -> {
            mAdapter.setData(contacts);
        });
    }

    @OnClick(R.id.call_cgroup)
    public void callCGroup(View v) {
        //TODO implement
    }
}
