package com.chooloo.www.koler.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.contacts.ContactsFragment
import com.chooloo.www.koler.ui.recents.RecentsFragment

class MainPagerAdapter(private val activity: BaseActivity) : FragmentStateAdapter(activity) {
    private val _isCompactPref by lazy { activity.boundComponent.preferencesInteractor.isCompact }

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> RecentsFragment.newInstance(isCompact = _isCompactPref, isSearchable = true)
            else -> ContactsFragment.newInstance(_isCompactPref, true)
        }
    }
}