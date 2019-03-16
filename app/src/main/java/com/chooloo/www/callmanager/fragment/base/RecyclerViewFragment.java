package com.chooloo.www.callmanager.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chooloo.www.callmanager.activity.AppBarActivity;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewFragment extends Fragment {
    protected RecyclerView mRecyclerView;

    @Nullable
    @Override
    @CallSuper
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = getRecyclerView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

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

    public abstract RecyclerView getRecyclerView();
}
