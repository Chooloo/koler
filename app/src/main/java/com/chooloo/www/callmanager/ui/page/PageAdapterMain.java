package com.chooloo.www.callmanager.ui.page;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapterMain extends FragmentStateAdapter {

    public PageAdapterMain(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @NonNull
    @Override
    public PageFragment createFragment(int position) {
        if (position == 1) {
            return PageRecents.newInstance();
        }
        return PageContacts.newInstance();
    }

}
