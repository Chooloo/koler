package com.chooloo.www.koler.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chooloo.www.koler.ui.contacts.ContactsFragment
import com.chooloo.www.koler.ui.recents.RecentsFragment
import com.chooloo.www.koler.util.preferences.KolerPreferences

class MainPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    private val isCompactPref by lazy { KolerPreferences(fragmentActivity).isCompact }
    override fun getItemCount() = 2
    override fun createFragment(position: Int) = when (position) {
        1 -> RecentsFragment.newInstance(isCompactPref)
        else -> ContactsFragment.newInstance(isCompactPref)
    }
}