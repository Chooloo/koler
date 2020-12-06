package com.chooloo.www.callmanager.ui.page;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chooloo.www.callmanager.ui.page.PageContacts;
import com.chooloo.www.callmanager.ui.page.PageFragment;
import com.chooloo.www.callmanager.ui.page.PageRecents;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class PageAdapter extends FragmentStateAdapter {

    public PageAdapter(FragmentActivity fragmentActivity) {
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
                return PageRecents.newInstance();
            case 1:
                return PageContacts.newInstance();
            default:
                return PageContacts.newInstance();
        }
    }
}
