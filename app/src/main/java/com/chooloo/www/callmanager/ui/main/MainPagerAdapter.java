package com.chooloo.www.callmanager.ui.main;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.chooloo.www.callmanager.ui.page.PageContacts;
import com.chooloo.www.callmanager.ui.page.PageFragment;
import com.chooloo.www.callmanager.ui.page.PageRecents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {

    // -- Constants -- //
//    private Context mContext;

    private List<Class> mClasses = new ArrayList<>(Arrays.asList(PageRecents.class, PageContacts.class));
    private List<String> mTitles = new ArrayList<>(Arrays.asList("Recents", "Contacts"));

    public MainPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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
                return PageRecents.newInstance();
            case 1:
                return PageContacts.newInstance();
            default:
                return PageContacts.newInstance();
        }
        // return (PageFragment) mClasses.get(position).getDeclaredConstructor(new Class[]{Context.class}).newInstance();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Contacts";
            case 1:
                return "Recents";
            default:
                return "Contacts";
        }
//        try {
//            return mTitles.get(position);
//        } catch (IndexOutOfBoundsException e) {
//            return null;
//        }
    }
}
