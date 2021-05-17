package com.chooloo.www.koler.adapter

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.contacts.ContactsFragment
import com.chooloo.www.koler.ui.recents.RecentsFragment
import com.chooloo.www.koler.util.preferences.KolerPreferences

class MainPagerAdapter(
    private val activity: BaseActivity
) : FragmentStateAdapter(activity) {
    private val isCompactPref by lazy { KolerPreferences(activity).compact }

    override fun getItemCount() = 2
    override fun createFragment(position: Int) = when (position) {
        1 -> RecentsFragment.newInstance(isCompactPref, true)
        else -> ContactsFragment.newInstance(isCompactPref, true)
    }
}