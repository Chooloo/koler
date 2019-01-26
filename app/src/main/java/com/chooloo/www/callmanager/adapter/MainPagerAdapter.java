package com.chooloo.www.callmanager.adapter;

import android.content.Context;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.fragment.DialFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final int[] TAB_LABELS_RES = new int[] {
            R.string.tab_label_dial
    };

    private Context mContext;
    private Fragment mFragment;

    public MainPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mFragment = new DialFragment();
                return mFragment;
            default:
                mFragment = new DialFragment();
                return mFragment;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(TAB_LABELS_RES[position]);
    }

    @Override
    public int getCount() {
        return TAB_LABELS_RES.length;
    }
}
