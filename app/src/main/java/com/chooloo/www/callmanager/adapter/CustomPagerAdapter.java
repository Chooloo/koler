package com.chooloo.www.callmanager.adapter;

import android.content.Context;

import com.chooloo.www.callmanager.ui.fragment.CGroupsFragment;
import com.chooloo.www.callmanager.ui.fragment.ContactsFragment;
import com.chooloo.www.callmanager.ui.fragment.RecentsFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;
    private int mNumItems;

    private Context mContext;

    private final static String FIRST_POSITION_TITLE = "Recents";
    private final static String SECOND_POSITION_TITLE = "Contacts";
    private final static String THIRD_POSITION_TITLE = "Excel";

    public CustomPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    @Override
    public int getCount() {
//        return this.mNumItems;
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RecentsFragment();
            case 1:
                return new ContactsFragment();
            case 2:
                return new CGroupsFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return FIRST_POSITION_TITLE;
            case 1:
                return SECOND_POSITION_TITLE;
            case 2:
                return THIRD_POSITION_TITLE;
            default:
                return null;
        }
    }
}
