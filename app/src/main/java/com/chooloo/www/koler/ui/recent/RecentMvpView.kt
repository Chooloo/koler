package com.chooloo.www.koler.ui.recent

import com.chooloo.www.koler.ui.base.MvpView

interface RecentMvpView : MvpView {
    fun callRecent()
    fun smsRecent()
    fun deleteRecent()
    fun animateLayout()
}