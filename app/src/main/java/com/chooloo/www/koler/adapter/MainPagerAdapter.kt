package com.chooloo.www.koler.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.contacts.ContactsFragment
import com.chooloo.www.koler.ui.recents.RecentsFragment

class MainPagerAdapter(activity: BaseActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> RecentsFragment.newInstance(isSearchable = true)
            else -> ContactsFragment.newInstance(isSearchable = true)
        }
    }
}