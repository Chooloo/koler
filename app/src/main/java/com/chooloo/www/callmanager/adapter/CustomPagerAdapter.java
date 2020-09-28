package com.chooloo.www.callmanager.adapter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.ui.fragment.CGroupsFragment;
import com.chooloo.www.callmanager.ui.fragment.ContactsFragment;
import com.chooloo.www.callmanager.ui.fragment.ContactsPageFragment;
import com.chooloo.www.callmanager.ui.fragment.RecentsFragment;
import com.chooloo.www.callmanager.ui.fragment.RecentsPageFragment;
import com.chooloo.www.callmanager.util.PreferenceUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    // -- Constants -- //
    private Context mContext;

    private List<Class> mClasses = new ArrayList<>(Arrays.asList(RecentsPageFragment.class, ContactsPageFragment.class, CGroupsFragment.class));
    private List<String> mTitles = new ArrayList<>(Arrays.asList("Recents", "Contacts", "Excel"));

    /**
     * Constructor
     *
     * @param context
     * @param fragmentManager
     */
    public CustomPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mContext = context;

        // Enable excel tab by the preference
        PreferenceUtils.getInstance(this.mContext);
        boolean isEnableExcel = PreferenceUtils.getInstance().getBoolean(R.string.pref_excel_enable_key);
        this.toggleExcelTab(isEnableExcel);
    }

    /**
     * Toggle excel tab by a given boolean
     *
     * @param isShow show or don't show the excel tab
     */
    private void toggleExcelTab(boolean isShow) {
        if (!isShow && mClasses.contains(CGroupsFragment.class)) {
            this.mClasses.remove(CGroupsFragment.class);
            this.mTitles.remove("Excel");
        } else if (isShow && !mClasses.contains(CGroupsFragment.class)) {
            this.mClasses.add(CGroupsFragment.class);
            this.mTitles.add("Excel");
        }
    }

    /**
     * Returns the amount of pages
     *
     * @return
     */
    @Override
    public int getCount() {
        return mClasses.size();
    }

    /**
     * Returns an item by its position
     *
     * @param position the position of the page
     * @return Fragment the fragment representing the page itself
     */
    @Override
    public Fragment getItem(int position) {
        try {
            return (Fragment) mClasses.get(position).getDeclaredConstructor(new Class[]{Context.class}).newInstance(mContext);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the pages title by his position
     *
     * @param position position of the page
     * @return String the string representing the title of the page
     */
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
