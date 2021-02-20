package com.chooloo.www.koler.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chooloo.www.koler.ui.contacts.ContactsFragment
import com.chooloo.www.koler.ui.recents.RecentsFragment

class MainPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 2
    override fun createFragment(position: Int) = when (position) {
        0 -> ContactsFragment.newInstance()
        1 -> RecentsFragment.newInstance()
        else -> ContactsFragment.newInstance()
    }
}