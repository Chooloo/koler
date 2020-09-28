package com.chooloo.www.callmanager.ui.fragment.base;

import com.chooloo.www.callmanager.ui.FABCoordinator;
import com.chooloo.www.callmanager.ui.activity.MainActivity;

public abstract class AbsPageFragment extends AbsBaseFragment implements
        FABCoordinator.FABDrawableCoordination,
        FABCoordinator.OnFABClickListener {

    @Override
    protected void onFragmentReady() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.syncFABAndFragment();
        }
    }
}
