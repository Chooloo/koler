package com.chooloo.www.callmanager.ui.about

import com.chooloo.www.callmanager.ui.base.MvpPresenter

interface AboutMvpPresenter<V : AboutMvpView?> : MvpPresenter<V> {
    fun onChangelogClick()
    fun onOpenSourceClick()
    fun onSendEmailClick()
    fun onReportBugClick()
    fun onRateAppClick()
    fun onDonateClick()
}