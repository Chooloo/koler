package com.chooloo.www.callmanager.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chooloo.www.callmanager.ui.page.PageContacts;
import com.chooloo.www.callmanager.ui.page.PageFragment;
import com.chooloo.www.callmanager.ui.page.PageRecents;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @NonNull
    @Override
    public PageFragment createFragment(int position) {
        switch (position) {
            case 0:
                return PageContacts.newInstance();
            case 1:
                return PageRecents.newInstance();
            default:
                return PageContacts.newInstance();
        }
    }

}
