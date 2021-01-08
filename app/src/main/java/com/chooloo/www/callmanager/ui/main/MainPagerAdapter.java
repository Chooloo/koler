package com.chooloo.www.callmanager.ui.main;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chooloo.www.callmanager.ui.page.PageContacts;
import com.chooloo.www.callmanager.ui.page.PageFragment;
import com.chooloo.www.callmanager.ui.page.PageRecents;

import timber.log.Timber;

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
        if (position == 1) {
            Timber.i("Returning recents");
            return PageRecents.newInstance();
        }
        Timber.i("Returning contacts");
        return PageContacts.newInstance();
    }

}
