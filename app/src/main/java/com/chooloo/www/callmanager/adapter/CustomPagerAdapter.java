package com.chooloo.www.callmanager.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.CGroupsFragment;
import com.chooloo.www.callmanager.ui.fragment.ContactsFragment;
import com.chooloo.www.callmanager.ui.fragment.ContactsTryFragment;
import com.chooloo.www.callmanager.ui.fragment.RecentsFragment;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    // -- Constants -- //
    private final static int NUM_ITEMS = 3;
    private Context mContext;

    // Fragment titles
    private static String FIRST_POSITION_TITLE = "Recents";
    private static String SECOND_POSITION_TITLE = "Contacts";
    private static String THIRD_POSITION_TITLE = "Excel";

    /**
     * Constructor
     *
     * @param fragmentManager
     */
    public CustomPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mContext = context;
    }

    /**
     * Returns the amount of pages
     *
     * @return
     */
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    /**
     * Returns an item by its position
     *
     * @param position
     * @return Fragment
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RecentsFragment();
            case 1:
                return new ContactsTryFragment(mContext);
            case 2:
                return new CGroupsFragment();
            default:
                return null;
        }
    }

    /**
     * Returns the pages title by his position
     *
     * @param position
     * @return String
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return this.mContext.getString(R.string.recents);
            case 1:
                return this.mContext.getString(R.string.contacts);
            case 2:
                return this.mContext.getString(R.string.excel);
            default:
                return null;
        }
    }
}
