package com.chooloo.www.callmanager.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.contacts.ContactsAdapter;
import com.chooloo.www.callmanager.ui.contacts.ContactsFragment;
import com.chooloo.www.callmanager.ui.page.PageContacts;
import com.chooloo.www.callmanager.ui.page.PageFragment;
import com.chooloo.www.callmanager.ui.page.PageRecents;
import com.chooloo.www.callmanager.ui.recents.RecentsAdapter;
import com.chooloo.www.callmanager.ui.recents.RecentsFragment;
import com.chooloo.www.callmanager.util.PreferenceUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    // -- Constants -- //
    private Context mContext;

    private List<Class> mClasses = new ArrayList<>(Arrays.asList(PageRecents.class, PageContacts.class));
    private List<String> mTitles = new ArrayList<>(Arrays.asList("Recents", "Contacts"));

    public CustomPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mContext = context;
    }

    @Override
    public int getCount() {
//        return mClasses.size();
        return 2;
    }

    @Override
    public PageFragment getItem(int position) {
        switch (position) {
            case 0:
                return PageContacts.newInstance();
            case 1:
                return PageRecents.newInstance();
            default:
                return PageContacts.newInstance();
        }
        // return (PageFragment) mClasses.get(position).getDeclaredConstructor(new Class[]{Context.class}).newInstance();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        try {
            return mTitles.get(position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
}
