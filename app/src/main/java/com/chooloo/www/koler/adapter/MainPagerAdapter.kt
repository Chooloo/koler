package com.chooloo.www.koler.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chooloo.www.koler.ui.page.PageContacts
import com.chooloo.www.koler.ui.page.PageFragment
import com.chooloo.www.koler.ui.page.PageRecents

class MainPagerAdapter(fragmentActivity: FragmentActivity?) : FragmentStateAdapter(fragmentActivity!!) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): PageFragment {
        return when (position) {
            0 -> PageContacts.newInstance()
            1 -> PageRecents.newInstance()
            else -> PageContacts.newInstance()
        }
    }
}