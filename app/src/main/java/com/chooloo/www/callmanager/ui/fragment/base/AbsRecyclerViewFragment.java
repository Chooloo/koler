package com.chooloo.www.callmanager.ui.fragment.base;

import androidx.recyclerview.widget.RecyclerView;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.activity.MainActivity;

import butterknife.BindView;

public abstract class AbsRecyclerViewFragment extends AbsBaseFragment {

    public @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.syncFABAndFragment();
        }
    }
}
