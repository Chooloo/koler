package com.chooloo.www.callmanager.fragment.base;

import android.os.Bundle;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.activity.AppBarActivity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public abstract class AbsRecyclerViewFragment extends BaseFragment {

    public @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof AppBarActivity) {
            AppBarActivity activity = (AppBarActivity) getActivity();
            mRecyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                activity.onVerticalScroll(mRecyclerView.canScrollVertically(-1)); //Whether the RecyclerView can scroll up
            });
        }
    }
}
